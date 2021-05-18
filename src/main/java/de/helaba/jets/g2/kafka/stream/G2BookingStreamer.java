package de.helaba.jets.g2.kafka.stream;

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
import org.apache.kafka.streams.kstream.KStream;
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

        final KStream<String, GenericRecord> kStream =
                builder
                        .stream(g2BookingTopicName, Consumed.with(Serdes.String(), valueGenericAvroSerde));

        //kStream
        //        .map((key, value) -> { // do something with each msg, square the values in our case
        //            //return KeyValue.pair(key, value * value);
        //            LOG.info(String.format("Received stream row with key %s: %s", key, value.toString()));
        //            return null;
        //        });

        return kStream;
    }

}
