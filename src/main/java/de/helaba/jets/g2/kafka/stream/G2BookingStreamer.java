package de.helaba.jets.g2.kafka.stream;

import de.helaba.jets.g2.kafka.avro.model.AvroG2BookingRecord;
import de.helaba.jets.g2.kafka.avro.model.AvroMessageType;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class G2BookingStreamer {

    private static final Log LOG = LogFactory.getLog(G2BookingStreamer.class);

    @Value(value = "${kafka.schemaRegistry.url}") // configured in application.properties
    private String schemaRegistryUrl;

    @Value(value = "${kafka.topic.g2Booking.name}") // configured in application.properties
    private String g2BookingTopicName;

    @Value(value = "${kafka.stream.store.g2BookingsCounter}") // configured in application.properties
    private String g2BookingsCounterStoreName;

    @Bean
    public java.util.function.Consumer<KStream<String, AvroG2BookingRecord>> process() {
        return input ->
                input.foreach((key, value) -> {
                    System.out.println("Key: " + key + " Value: " + value);
                });
    }

    @Bean("g2BookingStream")
    public KStream<String, AvroG2BookingRecord> createG2BookingStream(@Qualifier("g2BookingStreamBuilderFactory") StreamsBuilder builder) {
        final Map<String, String> serdeConfig =
                Collections.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        final SpecificAvroSerde<AvroG2BookingRecord> g2BookingSerde = new SpecificAvroSerde<>();
        g2BookingSerde.configure(serdeConfig, false); // `false` for record values

        // all G2Bookings
        final KStream<String, AvroG2BookingRecord> g2BookingRecords =
                builder
                        .stream(g2BookingTopicName, Consumed.with(Serdes.String(), g2BookingSerde))
                        // filter is used for logging
                        .filter(
                                (kopfnummer, record) -> {
                                    LOG.info(
                                            String.format(
                                                    "Stream g2BookingRecords: Received record: %s",
                                                    record.toString()
                                            )
                                    );
                                    return true;
                                }
                        );

        return g2BookingRecords;
    }

    @Bean("relevantG2BookingStream")
    public KStream<String, AvroG2BookingRecord> createRelevantG2BookingStream(@Qualifier("g2BookingStream") KStream<String, AvroG2BookingRecord> g2BookingRecords) {
        // all G2Bookings for pacs.003 and pacs.008
        final KStream<String, AvroG2BookingRecord> relevantG2BookingRecords =
                g2BookingRecords
                        .filter(
                                (kopfnummer, record) -> {
                                    AvroMessageType messageType = record.getMessageType();
                                    switch (messageType) {
                                        case PACS003:
                                        case PACS008:
                                            return true;
                                        default:
                                            return false;
                                    }
                                }
                        )
                        // filter is used for logging
                        .filter(
                                (kopfnummer, record) -> {
                                    LOG.info(
                                            String.format(
                                                    "Stream relevantG2BookingRecords: Received record: %s",
                                                    record.toString()
                                            )
                                    );
                                    return true;
                                }
                        );

        return relevantG2BookingRecords;
    }

    @Bean("groupedRelevantG2BookingStream")
    public KTable<String, Long> createG2BookingStream(@Qualifier("relevantG2BookingStream") KStream<String, AvroG2BookingRecord> relevantG2BookingRecords) {
        final Map<String, String> serdeConfig =
                Collections.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        final SpecificAvroSerde<AvroG2BookingRecord> g2BookingSerde = new SpecificAvroSerde<>();
        g2BookingSerde.configure(serdeConfig, false); // `false` for record values

        final KTable<String, Long> g2BookingRecordsGrouped =
                relevantG2BookingRecords
                        .groupBy((kopfnummer, record) -> kopfnummer, Grouped.with(Serdes.String(), g2BookingSerde))
                        // create a key-value store
                        .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as(g2BookingsCounterStoreName)
                                           .withKeySerde(Serdes.String())
                                           .withValueSerde(Serdes.Long()));

        return g2BookingRecordsGrouped;
    }

    //@Bean
    //public StoreBuilder g2BookingsCounterStore() {
    //    return
    //            Stores.keyValueStoreBuilder(
    //                    Stores.persistentKeyValueStore(g2BookingsCounterStoreName),
    //                    Serdes.String(),
    //                    Serdes.Long()
    //            );
    //}

    /*
    @KafkaStreamsStateStore(name="mystate", type= KafkaStreamsStateStoreProperties.StoreType.WINDOW, lengthMs=300000)
    public void process(KStream<String, AvroG2BookingRecord> g2BookingRecords) {
        final Map<String, String> serdeConfig =
                Collections.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        final SpecificAvroSerde<AvroG2BookingRecord> g2BookingSerde = new SpecificAvroSerde<>();
        g2BookingSerde.configure(serdeConfig, false); // `false` for record values

        // all G2Bookings for pacs.003 and pacs.008
        final KStream<String, AvroG2BookingRecord> relevantG2BookingRecords =
                g2BookingRecords
                        .filter(
                                (kopfnummer, record) -> {
                                    AvroMessageType messageType = record.getMessageType();
                                    switch (messageType) {
                                        case PACS003:
                                        case PACS008:
                                            return true;
                                        default:
                                            return false;
                                    }
                                }
                        )
                        // filter is used for logging
                        .filter(
                                (kopfnummer, record) -> {
                                    LOG.info(
                                            String.format(
                                                    "Stream relevantG2BookingRecords: Received record: %s",
                                                    record.toString()
                                            )
                                    );
                                    return true;
                                }
                        );

        //KGroupedStream<String, String> groupedByWord = textLines
        //        .flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
        //        .groupBy((key, word) -> word, Grouped.with(stringSerde, stringSerde));
        final KGroupedStream<String, AvroG2BookingRecord> g2BookingRecordsGrouped =
                relevantG2BookingRecords
                        .groupBy((kopfnummer, record) -> kopfnummer, Grouped.with(Serdes.String(), g2BookingSerde));

        // Create a key-value store
        g2BookingRecordsGrouped.count(Materialized.as(g2BookingsCounterStoreName));
    }
     */

}
