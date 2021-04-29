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
        G2BookingPayload g2BookingPayload = G2BookingPayloadUtil.random();
        try {
            g2BookingProducer.send(g2BookingPayload);
            Notification.show(String.format("Sent event %s to kafka topic.", g2BookingPayload));
        } catch(Exception e) {
            e.printStackTrace();
            Notification.show(String.format("Error sending event %s to kafka topic: %s", g2BookingPayload, e.getMessage()));
        }
    }

    public static G2BookingPayload fromAvroRecord(AvroG2BookingRecord record) {
        return
                new G2BookingPayloadBuilder()
                        .withKopfnummer(String.valueOf(record.getKopfnummer()))
                        .withMessageType(MessageType.valueOf(record.getMessageType().name()))
                        .withCreditorIban(String.valueOf(record.getCreditorIban()))
                        .withDebtorIban(String.valueOf(record.getDebtorIban()))
                        .withAmount(String.valueOf(record.getAmount()))
                        .withBatchBooking(record.getIsBatchBooking())
                        .withNoOfBatchBookingTransactions(record.getNoOfBatchBookingTransactions())
                        .withBookingDate(record.getBookingDate())
                        .withValuta(record.getValuta())
                        .build();
    }

}
