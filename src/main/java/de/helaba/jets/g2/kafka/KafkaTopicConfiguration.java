package de.helaba.jets.g2.kafka;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

// @Configuration
public class KafkaTopicConfiguration {

    private static final Log LOG = LogFactory.getLog(KafkaTopicConfiguration.class);

    @Value("${kafka.bootstrapAddress}")
    private String brokersUrl;

    @Value(value = "${kafka.topic.g2Booking.name}") // configured in application.properties
    private String topicG2Booking;

    @Value(value = "${kafka.topic.g2Booking.noOfPartitions}") // configured in application.properties
    private int topicG2BookingNoOfPartitions;

    @Value(value = "${kafka.topic.g2Booking.replicationFactor}") // configured in application.properties
    private int topicG2BookingReplicationFactor;

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, brokersUrl);
        return new KafkaAdmin(configs);
    }

    /**
     * Avoid manually creation of topics.
     *
     * @return created topic
     */
    @Bean
    public NewTopic topicG2Booking() {
        LOG.info(
                String.format(
                        "Creating kafka topic %s (noOfPartitions=%d, replicationFactor=%d)...",
                        topicG2Booking,
                        topicG2BookingNoOfPartitions,
                        topicG2BookingReplicationFactor
                )
        );
        NewTopic newTopic =
                TopicBuilder
                        .name(topicG2Booking)
                        .partitions(topicG2BookingNoOfPartitions)
                        .replicas(topicG2BookingReplicationFactor)
                        .build();
        LOG.info(String.format("Created kafka topic %s.", topicG2Booking));
        return newTopic;
    }

}