package de.helaba.jets.g2.kafka.producer;

import de.helaba.jets.g2.kafka.avro.model.G2BookingAvroRecord;
import de.helaba.jets.g2.kafka.event.G2BookingPayload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component // to be used as @Autowired component
public class G2BookingProducer {

    private static final Log LOG = LogFactory.getLog(G2BookingProducer.class);

    @Autowired // defined in KafkaProducerConfig
    private KafkaTemplate<String, G2BookingAvroRecord> g2BookingKafkaTemplate;

    @Value(value = "${kafka.topic.g2Booking.name}") // configured in application.properties
    private String g2BookingTopicName;

    public void send(G2BookingPayload g2BookingPayload) {
        ListenableFuture<SendResult<String, G2BookingAvroRecord>>future =
                g2BookingKafkaTemplate.send(g2BookingTopicName, g2BookingPayload.toAvroRecord());

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, G2BookingAvroRecord> result) {
                LOG.info(
                        String.format(
                                "Sent event=[%s] with offset=[%d]",
                                g2BookingPayload,
                                result.getRecordMetadata().offset()
                        )
                );
            }

            @Override
            public void onFailure(Throwable ex) {
                LOG.error(
                        String.format("Unable to send event=[%s]!", g2BookingPayload),
                        ex
                );
            }
        });
    }

}
