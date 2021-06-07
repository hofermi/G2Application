package de.helaba.jets.g2.kafka.stream;

import de.helaba.jets.g2.kafka.avro.model.AvroG2BookingRecord;
import de.helaba.jets.g2.kafka.avro.model.AvroMessageType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Predicate;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class G2BookingStreamer {

    private static final Log LOG = LogFactory.getLog(G2BookingStreamer.class);

    @Value(value = "${kafka.stream.store.g2BookingsCounter}") // configured in application.properties
    private String g2BookingsCounterStoreName;

    @Bean
    /*
     * Configured in application.properties:
     * spring.cloud.stream.bindings.newG2BookingStream-in-0...
     * spring.cloud.stream.kafka.streams.bindings.newG2BookingStream-in-0...
     * spring.cloud.stream.kafka.streams.bindings.newG2BookingStream-in-0...
     */
    public java.util.function.Consumer<KStream<String, AvroG2BookingRecord>> newG2BookingStream() {
        return
                input ->
                        relevantG2BookingRecordsCount(
                                input
                                        // log received records
                                        .filter(logMessagesPredicate("Received record: "))
                                        // filter relevant message types
                                        .filter(filterRelevantMsgTypesPredicate())
                                        // log received relevant records
                                        .filter(logMessagesPredicate("Received relevant record: "))
                        );
    }

    private Predicate<String, AvroG2BookingRecord> logMessagesPredicate(String logPrefix) {
        return
                (kopfnummer, record) -> {
                    LOG.info(logPrefix + record.toString());
                    return true;
                };
    }

    private Predicate<String, AvroG2BookingRecord> filterRelevantMsgTypesPredicate() {
        return
                (kopfnummer, record) -> {
                    AvroMessageType messageType = record.getMessageType();
                    switch (messageType) {
                        case PACS003:
                        case PACS008:
                            return true;
                        default:
                            return false;
                    }
                };
    }

    private void relevantG2BookingRecordsCount(KStream<String, AvroG2BookingRecord> relevantG2BookingRecords) {
        //final Map<String, String> serdeConfig =
        //        Collections.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        //final SpecificAvroSerde<AvroG2BookingRecord> g2BookingSerde = new SpecificAvroSerde<>();
        //g2BookingSerde.configure(serdeConfig, false); // `false` for record values

        relevantG2BookingRecords
                //.groupBy((kopfnummer, record) -> kopfnummer, Grouped.with(Serdes.String(), g2BookingSerde))
                .groupByKey()
                // write to key-value store => KTable<String, Long>
                .count(
                        Materialized
                                .<String, Long, KeyValueStore<Bytes, byte[]>>as(g2BookingsCounterStoreName)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(Serdes.Long())
                );
    }

}
