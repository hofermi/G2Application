package de.helaba.jets.g2.kafka;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfiguration {

    private static final Log LOG = LogFactory.getLog(KafkaTopicConfiguration.class);

    @Value(value = "${kafka.topic.g2Booking.name}") // configured in application.properties
    private String topicG2Booking;

    @Value(value = "${kafka.topic.g2Booking.noOfPartitions}") // configured in application.properties
    private int topicG2BookingNoOfPartitions;

    @Value(value = "${kafka.topic.g2Booking.replicationFactor}") // configured in application.properties
    private int topicG2BookingReplicationFactor;

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
        try {
            return
                    TopicBuilder
                            .name(topicG2Booking)
                            .partitions(topicG2BookingNoOfPartitions)
                            .replicas(topicG2BookingReplicationFactor)
                            .build();
        } finally {
            LOG.info(String.format("Created kafka topic %s.", topicG2Booking));
        }
    }

}