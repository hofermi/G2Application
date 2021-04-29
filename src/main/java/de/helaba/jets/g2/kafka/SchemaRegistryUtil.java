package de.helaba.jets.g2.kafka;

import de.helaba.jets.g2.kafka.consumer.G2BookingConsumer;
import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.apache.avro.Schema;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SchemaRegistryUtil {

    private static final Log LOG = LogFactory.getLog(SchemaRegistryUtil.class);

    // schema registry url
    private final static String SCHEMA_REGISTRY_URL = "http://localhost:8081";

    private final static String TOPIC_NAME = "g2-booking";
    private final static String SCHEMA_PATH = "src/main/resources/avro/g2booking-schema.avsc";

    public static void main(String... args) throws RestClientException, IOException {
        registerSchema(TOPIC_NAME, SCHEMA_PATH);
    }

    private static void registerSchema(String topic, String schemaPath) throws IOException, RestClientException {
        Integer currentSchemaVersion = getRegisteredSchemaVersion(topic);
        if (currentSchemaVersion != null) {
            LOG.info(String.format("Schema already registered (version %d).", currentSchemaVersion));
            return;
        }

        String subject = getSubjectNameForTopic(topic);
        CachedSchemaRegistryClient schemaRegistryClient = new CachedSchemaRegistryClient(SCHEMA_REGISTRY_URL, 20);

        // avsc json string
        String schema = Files.readString(Path.of(schemaPath));
        Schema avroSchema = new Schema.Parser().parse(schema);
        int id = schemaRegistryClient.register(subject, avroSchema);
        int version = schemaRegistryClient.getLatestSchemaMetadata(subject).getVersion();
        LOG.info(String.format("Registered schema for topic '%s' (id=%d, version=%d).", topic, id, version));
    }

    private static Integer getRegisteredSchemaVersion(String topic) throws IOException, RestClientException {
        CachedSchemaRegistryClient schemaRegistryClient = new CachedSchemaRegistryClient(SCHEMA_REGISTRY_URL, 20);

        // subject convention is "<topic-name>-value"
        String subject = getSubjectNameForTopic(topic);

        try {
            return schemaRegistryClient.getLatestSchemaMetadata(subject).getVersion();
        } catch(RestClientException e) {
            if (e.getMessage().contains("Subject not found")) { // Subject not found.; error code: 40401
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
