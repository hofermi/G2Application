package de.helaba.jets.g2.kafka.consumer;

import de.helaba.jets.g2.kafka.avro.model.AvroG2BookingRecord;
import de.helaba.jets.g2.kafka.event.G2BookingPayload;
import de.helaba.jets.g2.kafka.event.G2BookingPayloadUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component // mandatory for using @KafkaListener
public class G2BookingConsumer {

    private static final Log LOG = LogFactory.getLog(G2BookingConsumer.class);

    @KafkaListener(
            id = "${kafka.topic.g2Booking.consumer.groupId}",
            topics = "${kafka.topic.g2Booking.name}",
            clientIdPrefix = "myClientId",
            autoStartup = "true"
    )
    public void listen(
            @Payload AvroG2BookingRecord data,
            ConsumerRecordMetadata recordMetadata,
            Acknowledgment ack
    ) {
        G2BookingPayload g2BookingPayload = G2BookingPayloadUtil.fromAvroRecord(data);
        LOG.info(
                String.format(
                        "Received event=[%s] from [topic=%s, partition=%d, offset=%d]",
                        g2BookingPayload,
                        recordMetadata.topic(),
                        recordMetadata.partition(),
                        recordMetadata.offset()
                )
        );
        ack.acknowledge(); // async commit
    }

}
