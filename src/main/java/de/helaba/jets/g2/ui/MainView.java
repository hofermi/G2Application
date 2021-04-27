package de.helaba.jets.g2.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.helaba.jets.g2.kafka.event.G2BookingPayload;
import de.helaba.jets.g2.kafka.producer.G2BookingProducer;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * access main view via:
 * http://localhost:8080/
 */
@Route
public class MainView extends VerticalLayout {

    @Autowired
    G2BookingProducer g2BookingProducer;

    public MainView() {
        //add(new Button("Click me", e -> Notification.show("Hello, this is a G2 prototype!")));
        add(new Button("Send event to kafka topic", e -> sendRandomG2Booking()));
    }

    private void sendRandomG2Booking() {
        String creditorIban = String.format("DE%d020", RandomUtils.nextLong());
        String debtorIban = String.format("DE%d020", RandomUtils.nextLong());
        String amount = String.format("%.2f", RandomUtils.nextFloat(0.01f, 9999.99f));
        G2BookingPayload g2BookingPayload = new G2BookingPayload(creditorIban, debtorIban, amount);
        try {
            g2BookingProducer.send(g2BookingPayload);
            Notification.show(String.format("Sent event %s to kafka topic.", g2BookingPayload));
        } catch(Exception e) {
            e.printStackTrace();
            Notification.show(String.format("Error sending event %s to kafka topic: %s", g2BookingPayload, e.getMessage()));
        }
    }

}
