package de.helaba.jets.g2.kafka;

import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import io.confluent.kafka.streams.serdes.avro.GenericAvroSerde;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.FailOnInvalidTimestamp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

@Configuration
public class KafkaStreamConfiguration {

    private int threads = 1;
    private int replicationFactor = 1;

    @Value("${kafka.bootstrapAddress}")
    private String brokersUrl;

    @Value(value = "${kafka.schemaRegistry.url}") // configured in application.properties
    private String schemaRegistryUrl;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public StreamsConfig kStreamsConfigs() {
        Map<String, Object> config = new HashMap<>();
        setDefaults(config);
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "default");
        return new StreamsConfig(config);
    }

    public void setDefaults(Map<String, Object> config) {
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, GenericAvroSerde.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, FailOnInvalidTimestamp.class);
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, brokersUrl);
        config.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
    }

    @Bean("g2BookingStreamBuilder")
    public StreamsBuilderFactoryBean app1StreamBuilderFactoryBean() {
        Map<String, Object> config = new HashMap<>();
        setDefaults(config);
        config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "g2BookingStreamerId");
        config.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 30000);
        config.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, threads);
        config.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, replicationFactor);

        Properties properties = new Properties();
        config.forEach(properties::put);
        StreamsBuilderFactoryBean streamsBuilderFactoryBean = new StreamsBuilderFactoryBean();
        streamsBuilderFactoryBean.setStreamsConfiguration(properties);
        return streamsBuilderFactoryBean;
    }

}