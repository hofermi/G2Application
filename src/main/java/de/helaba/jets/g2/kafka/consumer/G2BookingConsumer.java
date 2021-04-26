package de.helaba.jets.g2.kafka.consumer;

import de.helaba.jets.g2.kafka.avro.model.G2BookingAvroRecord;
import de.helaba.jets.g2.kafka.event.G2BookingPayload;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component // mandatory for using @KafkaListener
public class G2BookingConsumer {

    @KafkaListener(
            id = "${kafka.topic.g2Booking.consumer.groupId}",
            topics = "${kafka.topic.g2Booking.name}",
            clientIdPrefix = "myClientId",
            autoStartup = "true"
    )
    public void listen(G2BookingAvroRecord data, Acknowledgment ack) {
        G2BookingPayload g2BookingPayload = G2BookingPayload.fromAvroRecord(data);
        System.out.println("Received: " + g2BookingPayload);
        ack.acknowledge(); // async commit
    }

/*
    public void consume() {
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(BasicKafkaConstants.CONFIG_CONSUMER)) {
            consumer.subscribe(Set.of(g2BookingTopicName));

            while (true) {
                // wait a max. time of 100 millis for records
                // do not wait any longer, if (a batch of) records are received
                final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.format("Got record with value %s%n", record.value());
                }
                consumer.commitAsync();
            }
        }
    }
*/
}
