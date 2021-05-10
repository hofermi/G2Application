package de.helaba.jets.g2.kafka;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.avro.Schema;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaSchemaConfiguration {

    private static final Log LOG = LogFactory.getLog(KafkaSchemaConfiguration.class);

    @Value(value = "${kafka.schemaRegistry.url}") // configured in application.properties
    private String schemaRegistryUrl;

    @Value(value = "${kafka.topic.g2Booking.name}") // configured in application.properties
    private String topicG2Booking;

    @Value(value = "${kafka.topic.g2Booking.schema}") // configured in application.properties
    private String topicG2BookingSchema;

    /**
     * Avoid manually creation of topics.
     *
     * @return created topic
     */
    @Bean
    public void schemaG2Booking() throws RestClientException, IOException {
        LOG.info(String.format("Creating schema for kafka topic %s...", topicG2Booking));
        try {
            registerSchema(topicG2Booking, topicG2BookingSchema);
        } finally {
            LOG.info(String.format("Created schema for kafka topic %s.", topicG2Booking));
        }
    }

    private void registerSchema(String topic, String schemaPath) throws IOException, RestClientException {
        Integer currentSchemaVersion = getRegisteredSchemaVersion(topic);
        if (currentSchemaVersion != null) {
            LOG.info(String.format("Schema already registered (version %d).", currentSchemaVersion));
            return;
        }

        String subject = getSubjectNameForTopic(topic);
        CachedSchemaRegistryClient schemaRegistryClient = new CachedSchemaRegistryClient(schemaRegistryUrl, 20);

        // avsc json string
        String schema = Files.readString(Path.of(schemaPath));
        Schema avroSchema = new Schema.Parser().parse(schema);
        int id = schemaRegistryClient.register(subject, avroSchema);
        int version = schemaRegistryClient.getLatestSchemaMetadata(subject).getVersion();
        LOG.info(String.format("Registered schema for kafka topic %s (id=%d, version=%d).", topic, id, version));
    }

    private Integer getRegisteredSchemaVersion(String topic) throws IOException, RestClientException {
        CachedSchemaRegistryClient schemaRegistryClient = new CachedSchemaRegistryClient(schemaRegistryUrl, 20);

        // subject convention is "<topic-name>-value"
        String subject = getSubjectNameForTopic(topic);

        try {
            return schemaRegistryClient.getLatestSchemaMetadata(subject).getVersion();
        } catch(RestClientException e) {
            if (e.getMessage().contains(String.format("Subject '%s' not found", subject))) { // Subject not found.; error code: 40401
                return null;
            }
            throw e;
        }
    }

    private static String getSubjectNameForTopic(String topic) {
        // subject convention is "<topic-name>-value"
        return topic + "-value";
    }

}