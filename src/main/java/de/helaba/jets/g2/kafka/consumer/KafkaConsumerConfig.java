package de.helaba.jets.g2.kafka.consumer;

import de.helaba.jets.g2.kafka.avro.AvroDeserializer;
import de.helaba.jets.g2.kafka.avro.model.G2BookingAvroRecord;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
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

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${kafka.bootstrapAddress}") // configured in application.properties
    private String bootstrapAddress;

    @Value(value = "${kafka.topic.g2Booking.consumer.groupId}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, G2BookingAvroRecord> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, AvroDeserializer.class);

        // read all existing records from the topic
        // use "latest" to do not read any prior records
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // do not auto-commit every five seconds (this would be the default)
        // instead use consumer.commitAsync() to commit offsets
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new AvroDeserializer<G2BookingAvroRecord>(G2BookingAvroRecord.class));
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, G2BookingAvroRecord>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, G2BookingAvroRecord> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        // number of consumers in consumer group
        factory.setConcurrency(1);

        // wait a max. time of 3000 millis for records
        // do not wait any longer, if (a batch of) records are received
        factory.getContainerProperties().setPollTimeout(3000);

        // manual committing offsets
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        return factory;
    }

}