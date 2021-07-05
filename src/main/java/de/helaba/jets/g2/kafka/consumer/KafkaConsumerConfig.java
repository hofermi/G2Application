package de.helaba.jets.g2.kafka.consumer;

import de.helaba.jets.g2.kafka.avro.model.AvroG2BookingRecord;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

//@EnableKafka
// @Configuration
public class KafkaConsumerConfig {

    @Value(value = "${kafka.bootstrapAddress}") // configured in application.properties
    private String bootstrapAddress;

    @Value(value = "${kafka.topic.g2Booking.consumer.groupId}") // configured in application.properties
    private String groupId;

    @Value(value = "${kafka.schemaRegistry.url}") // configured in application.properties
    private String schemaRegistryUrl;

    @Bean
    public ConsumerFactory<String, AvroG2BookingRecord> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);

        // use Specific Record, otherwise you get Avro GenericRecord.
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");

        // read all existing records from the topic
        // use "latest" to do not read any prior records
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // do not auto-commit every five seconds (this would be the default)
        // instead use consumer.commitAsync() to commit offsets
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return new DefaultKafkaConsumerFactory(props);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, AvroG2BookingRecord>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AvroG2BookingRecord> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        // number of consumers in consumer group
        factory.setConcurrency(1);

        // wait a max. time of 3000 millis for records
        // do not wait any longer, if (a batch of) records are received
        factory.getContainerProperties().setPollTimeout(3000);

        // manual committing offsets
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        // enable batch processing
        //factory.setBatchListener(true);

        return factory;
    }

}