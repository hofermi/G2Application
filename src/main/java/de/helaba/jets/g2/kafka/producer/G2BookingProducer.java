package de.helaba.jets.g2.kafka.producer;

import de.helaba.jets.g2.kafka.avro.model.AvroG2BookingRecord;
import de.helaba.jets.g2.kafka.event.G2BookingPayload;
import de.helaba.jets.g2.kafka.event.G2BookingPayloadUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component // to be used as @Autowired component
public class G2BookingProducer {

    private static final Log LOG = LogFactory.getLog(G2BookingProducer.class);

    @Autowired // defined in KafkaProducerConfig
    private KafkaTemplate<String, AvroG2BookingRecord> g2BookingKafkaTemplate;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Value(value = "${kafka.topic.g2Booking.name}") // configured in application.properties
    private String g2BookingTopicName;

    public void send(G2BookingPayload g2BookingPayload) {
        ListenableFuture<SendResult<String, AvroG2BookingRecord>>future =
                g2BookingKafkaTemplate.send(
                        g2BookingTopicName,
                        g2BookingPayload.getKopfnummer(), // key
                        G2BookingPayloadUtil.toAvroRecord(g2BookingPayload) // value
                );

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, AvroG2BookingRecord> result) {
                applicationEventPublisher.publishEvent(
                    new G2BookingPayloadSentEvent(this, G2BookingPayloadUtil.fromAvroRecord(result.getProducerRecord().value()))
                );
                //LOG.info(
                //        String.format(
                //                "Sent event=[%s] with offset=[%d]",
                //                g2BookingPayload,
                //                result.getRecordMetadata().offset()
                //        )
                //);
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
