package de.helaba.jets.g2.kafka.stream;

import de.helaba.jets.g2.kafka.avro.model.AvroG2BookingRecord;
import de.helaba.jets.g2.kafka.avro.model.AvroMessageType;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import java.util.Collections;
import java.util.Map;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KGroupedTable;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;
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

    @Bean("g2BookingStream")
    public KStream<String, GenericRecord> startProcessing(@Qualifier("g2BookingStreamBuilder") StreamsBuilder builder) {
        final Map<String, String> serdeConfig =
                Collections.singletonMap(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        final Serde<GenericRecord> valueGenericAvroSerde = new GenericAvroSerde();
        valueGenericAvroSerde.configure(serdeConfig, false); // `false` for record values

        final KStream<String, GenericRecord> g2BookingRecords =
                builder.stream(g2BookingTopicName, Consumed.with(Serdes.String(), valueGenericAvroSerde));

        final KStream<String, GenericRecord> relevantG2BookingRecords =
                g2BookingRecords
                        .filter(
                                (kopfnummer, record) -> {
                                    AvroMessageType messageType = ((AvroG2BookingRecord) record).getMessageType();
                                    switch (messageType) {
                                        case PACS003:
                                        case PACS008:
                                            return true;
                                        default:
                                            return false;
                                    }
                                }
                        );
        //.map((kopfnummer, record) -> KeyValue.pair(kopfnummer, ((AvroG2BookingRecord) record).getAmount()));

        final KGroupedStream<String, Long> g2BookingRecordsCounts =
                relevantG2BookingRecords
                        .toTable(Named.as("bookings"))
                        .groupBy((kopfnummer, record) -> kopfnummer)
                        .count(Named.as("no-of-bookings-per-kopfnummer"));

        //kStream
        //        .map((key, value) -> { // do something with each msg, square the values in our case
        //            //return KeyValue.pair(key, value * value);
        //            LOG.info(String.format("Received stream row with key %s: %s", key, value.toString()));
        //            return null;
        //        });

        KStream<String, PlayEvent> playEvents =
                builder.stream(Serdes.String(), playEventSerde, "play-events");

        KStream<Long, PlayEvent> playsBySongId =
                playEvents
                        .filter((region, event) -> event.getDuration() >= MIN_CHARTABLE_DURATION)
                        .map((key, value) -> KeyValue.pair(value.getSongId(), value));

        KStream<Long, Song> songPlays =
                playsBySongId.leftJoin(songTable, (playEvent, song) -> song, Serdes.Long(), playEventSerde);

        KGroupedTable<Long, Long> groupedBySongId =
                songPlays.groupBy((songId, song) -> songId, Serdes.Long(), Serdes.Long());
        groupedBySongId.count(Named.as("song-play-count"));

        return kStream;
    }

}
