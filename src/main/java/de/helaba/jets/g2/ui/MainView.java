package de.helaba.jets.g2.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.helaba.jets.g2.kafka.avro.model.AvroG2BookingRecord;
import de.helaba.jets.g2.kafka.event.G2BookingPayload;
import de.helaba.jets.g2.kafka.event.G2BookingPayloadBuilder;
import de.helaba.jets.g2.kafka.event.G2BookingPayloadUtil;
import de.helaba.jets.g2.kafka.event.MessageType;
import de.helaba.jets.g2.kafka.producer.G2BookingProducer;
import java.time.Duration;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * access main view via:
 * http://localhost:8080/
 */
@Route
public class MainView extends VerticalLayout {

    private final static int NO_OF_BATCH_EVENTS = 10;

    @Autowired
    G2BookingProducer g2BookingProducer;

    public MainView() {
        //add(new Button("Click me", e -> Notification.show("Hello, this is a G2 prototype!")));
        add(new Button("Send event to kafka topic", e -> sendRandomG2Bookings(1)));
        add(new Button(String.format("Send %d events to kafka topic", NO_OF_BATCH_EVENTS), e -> sendRandomG2Bookings(NO_OF_BATCH_EVENTS)));
    }

    private void sendRandomG2Bookings(int noOfEvents) {
        Instant start = Instant.now();
        for (int i = 0; i < noOfEvents; i++) {
            G2BookingPayload g2BookingPayload = G2BookingPayloadUtil.random();
            try {
                g2BookingProducer.send(g2BookingPayload);
                //Notification.show(String.format("Sent event %s to kafka topic.", g2BookingPayload));
            } catch (Exception e) {
                e.printStackTrace();
                Notification.show(String.format("Error sending event %s to kafka topic: %s", g2BookingPayload, e.getMessage()));
                return;
            }
        }
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        Notification.show(String.format("Sent %d events to kafka topic in %d millis.", noOfEvents, timeElapsed));
    }

}
