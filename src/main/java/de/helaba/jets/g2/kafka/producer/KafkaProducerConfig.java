package de.helaba.jets.g2.kafka.producer;

import de.helaba.jets.g2.kafka.avro.AvroSerializer;
import de.helaba.jets.g2.kafka.avro.model.G2BookingAvroRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

    @Value(value = "${kafka.bootstrapAddress}") // configured in application.properties
    private String bootstrapAddress;

    @Bean
    public ProducerFactory<String, G2BookingAvroRecord> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, AvroSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean // to be used as @Autowired component
    public KafkaTemplate<String, G2BookingAvroRecord> g2BookingKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}