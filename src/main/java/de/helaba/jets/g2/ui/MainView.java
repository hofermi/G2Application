package de.helaba.jets.g2.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.helaba.jets.g2.kafka.event.G2BookingPayload;
import de.helaba.jets.g2.kafka.event.G2BookingPayloadUtil;
import de.helaba.jets.g2.kafka.producer.G2BookingProducer;
import java.time.Duration;
import java.time.Instant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;

/**
 * access main view via:
 * http://localhost:8080/
 */
@Route
public class MainView extends VerticalLayout {

    private static final Log LOG = LogFactory.getLog(MainView.class);

    private final static int NO_OF_BATCH_EVENTS = 10;

    @Autowired
    private G2BookingProducer g2BookingProducer;

    @Autowired
    private InteractiveQueryService interactiveQueryService;

    @Value(value = "${kafka.stream.store.g2BookingsCounter}") // configured in application.properties
    private String g2BookingsCounterStoreName;

    public MainView() {
        //add(new Button("Click me", e -> Notification.show("Hello, this is a G2 prototype!")));
        add(new Button("Send event to kafka topic", e -> sendRandomG2Bookings(1)));
        add(new Button(String.format("Send %d events to kafka topic", NO_OF_BATCH_EVENTS), e -> sendRandomG2Bookings(NO_OF_BATCH_EVENTS)));
        add(new Button("Print current stream state stores", e -> printStreamStateStores()));
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

    private void printStreamStateStores() {
        ReadOnlyKeyValueStore<String, Long> keyValueStore =
                interactiveQueryService.getQueryableStore(
                        g2BookingsCounterStoreName,
                        QueryableStoreTypes.keyValueStore()
                );

        for (KeyValueIterator<String, Long> it = keyValueStore.all(); it.hasNext(); ) {
            KeyValue<String, Long> entry = it.next();
            LOG.info(String.format("%s: %s", entry.key, entry.value));
        }
    }

}
