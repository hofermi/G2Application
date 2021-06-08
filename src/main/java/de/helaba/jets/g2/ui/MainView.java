package de.helaba.jets.g2.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import de.helaba.jets.g2.kafka.event.G2BookingPayload;
import de.helaba.jets.g2.kafka.event.G2BookingPayloadUtil;
import de.helaba.jets.g2.kafka.producer.G2BookingPayloadSentEvent;
import de.helaba.jets.g2.kafka.producer.G2BookingProducer;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * access main view via:
 * http://localhost:8080/
 */
@Push
@Route
@PreserveOnRefresh
@Component // for receiving ApplicationEvents
public class MainView extends VerticalLayout {

    private final static int NO_OF_BATCH_EVENTS = 10;
    private final static int NO_OF_REMEMBERED_EVENTS = 100;

    @Autowired
    private G2BookingProducer g2BookingProducer;

    @Autowired
    private InteractiveQueryService interactiveQueryService;

    private ListDataProvider<G2BookingPayload> g2BookingPayloadDataProvider;
    private List<G2BookingPayload> g2BookingPayloadRows;

    private ListDataProvider<KopfnummerCountRow> kopfnummerCountDataProvider;
    private List<KopfnummerCountRow> kopfnummerCountRows;

    @Value(value = "${kafka.stream.store.g2BookingsCounter}") // configured in application.properties
    private String g2BookingsCounterStoreName;

    public MainView() {
        //add(new Button("Click me", e -> Notification.show("Hello, this is a G2 prototype!")));
        add(new Label(String.format("Zuletzt versendete Events (max. %d)", NO_OF_REMEMBERED_EVENTS)));
        add(createG2BookingPayloadGrid());
        add(new Button("Sende Event an Kafka-Topic", e -> sendRandomG2Bookings(1)));
        add(new Button(String.format("Sende %d Events an Kafka-Topic", NO_OF_BATCH_EVENTS), e -> sendRandomG2Bookings(NO_OF_BATCH_EVENTS)));
        add(new Label(""));
        add(new Label("Anzahl erhaltener Original-Nachrichten pro Kopfnummer"));
        add(createKopfnummerCountGrid());
        add(new Button("Aktualisiere Tabelle", e -> updateKopfnummerCountGrid()));
    }

    private Grid<G2BookingPayload> createG2BookingPayloadGrid() {
        g2BookingPayloadRows = new ArrayList<>();
        g2BookingPayloadDataProvider = DataProvider.ofCollection(g2BookingPayloadRows);

        Grid<G2BookingPayload> grid = new Grid<>(G2BookingPayload.class);
        grid.setHeight("200px");
        grid.removeAllColumns();
        grid.setDataProvider(g2BookingPayloadDataProvider);

        grid
                .addColumn(G2BookingPayload::getKopfnummer)
                .setHeader("Kopfnummer");
        grid
                .addColumn(G2BookingPayload::getMessageType)
                .setHeader("Message Type");
        grid
                .addColumn(G2BookingPayload::getCreditorIban)
                .setHeader("IBAN (Creditor)");
        grid
                .addColumn(G2BookingPayload::getDebtorIban)
                .setHeader("IBAN (Debtor)");
        grid
                .addColumn(G2BookingPayload::getBookingDate)
                .setHeader("Buchungstag");
        grid
                .addColumn(G2BookingPayload::getValuta)
                .setHeader("Valuta");
        grid
                .addColumn(G2BookingPayload::getAmount)
                .setHeader("Betrag");

        grid.setSortableColumns();
        return grid;
    }

    private Grid<KopfnummerCountRow> createKopfnummerCountGrid() {
        kopfnummerCountRows = new ArrayList<>();
        kopfnummerCountDataProvider = DataProvider.ofCollection(kopfnummerCountRows);
        kopfnummerCountDataProvider.setSortOrder(KopfnummerCountRow::getKopfnummer, SortDirection.ASCENDING);

        Grid<KopfnummerCountRow> grid = new Grid<>(KopfnummerCountRow.class);
        grid.setHeight("200px");
        grid.removeAllColumns();
        grid.setDataProvider(kopfnummerCountDataProvider);

        grid
                .addColumn(KopfnummerCountRow::getKopfnummer)
                .setHeader("Kopfnummer");
        grid
                .addColumn(KopfnummerCountRow::getCount)
                .setHeader("Anzahl");

        return grid;
    }

    private void appendToG2BookingPayloadGrid(G2BookingPayload payload) {
        g2BookingPayloadRows.add(0, payload);
        while (g2BookingPayloadRows.size() > NO_OF_REMEMBERED_EVENTS) {
            g2BookingPayloadRows.remove(g2BookingPayloadRows.size() - 1);
        }
        g2BookingPayloadDataProvider.refreshAll();
    }

    private void updateKopfnummerCountGrid() {
        kopfnummerCountRows.clear();
        kopfnummerCountRows.addAll(readStreamStateStores());
        kopfnummerCountDataProvider.refreshAll();
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

    private List<KopfnummerCountRow> readStreamStateStores() {
        ReadOnlyKeyValueStore<String, Long> keyValueStore =
                interactiveQueryService.getQueryableStore(
                        g2BookingsCounterStoreName,
                        QueryableStoreTypes.keyValueStore()
                );

        List<KopfnummerCountRow> rows = new ArrayList<>();
        for (KeyValueIterator<String, Long> it = keyValueStore.all(); it.hasNext(); ) {
            KeyValue<String, Long> entry = it.next();
            rows.add(new KopfnummerCountRow(entry.key, entry.value));
        }
        return rows;
    }

    @EventListener(G2BookingPayloadSentEvent.class)
    public void listenToG2BookingPayloadSentEvent(G2BookingPayloadSentEvent event) {
        getUI().ifPresent(ui -> ui.access(() -> {
            appendToG2BookingPayloadGrid(event.getG2BookingPayload());
        }));
    }

    private static class KopfnummerCountRow {
        private String kopfnummer;
        private Long count;

        public KopfnummerCountRow(String kopfnummer, Long count) {
            this.kopfnummer = kopfnummer;
            this.count = count;
        }

        public String getKopfnummer() {
            return kopfnummer;
        }

        public Long getCount() {
            return count;
        }
    }

}
