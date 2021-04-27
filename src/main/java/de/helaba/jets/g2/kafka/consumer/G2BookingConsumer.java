package de.helaba.jets.g2.kafka.consumer;

import de.helaba.jets.g2.kafka.avro.model.G2BookingAvroRecord;
import de.helaba.jets.g2.kafka.event.G2BookingPayload;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component // mandatory for using @KafkaListener
public class G2BookingConsumer {

    @KafkaListener(
            id = "${kafka.topic.g2Booking.consumer.groupId}",
            topics = "${kafka.topic.g2Booking.name}",
            clientIdPrefix = "myClientId",
            autoStartup = "true"
    )
    public void listen(
            @Payload G2BookingAvroRecord data,
            ConsumerRecordMetadata recordMetadata,
            Acknowledgment ack
    ) {
        G2BookingPayload g2BookingPayload = G2BookingPayload.fromAvroRecord(data);
        System.out.println(
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
