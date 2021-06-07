package de.helaba.jets.g2.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import de.helaba.jets.g2.kafka.avro.model.AvroG2BookingRecord;
import de.helaba.jets.g2.kafka.event.G2BookingPayload;
import de.helaba.jets.g2.kafka.event.G2BookingPayloadUtil;
import de.helaba.jets.g2.kafka.producer.G2BookingProducer;
import de.helaba.jets.g2.kafka.stream.G2BookingStreamer;
import java.time.Duration;
import java.time.Instant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.state.HostInfo;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;

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

    //@Autowired
    //private G2BookingStreamer g2BookingStreamer;

    //@Autowired
    //private KStream<String, AvroG2BookingRecord> g2BookingStream;

    @Autowired
    private InteractiveQueryService interactiveQueryService;

    //@Autowired
    //@Qualifier("oldG2BookingStreamBuilderFactory")
    //private StreamsBuilderFactoryBean oldG2BookingStreamBuilderFactory;

    //@Autowired
    //private KTable<String, Long> groupedRelevantG2BookingStream;

    //@Autowired
    //private KafkaStreamsRegistry kafkaStreamsRegistry;

    //@Autowired
    //private KafkaStreamsBinderConfigurationProperties binderConfigurationProperties;

    //@Autowired
    //private KafkaStreams kafkaStreams;

    //@Autowired
    //private ProcessorContext processorContext;

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
        //LOG.info("Store name: " + groupedRelevantG2BookingStream.queryableStoreName());
        //StateStore stateStore = processorContext.getStateStore(g2BookingsCounterStoreName);
        //LOG.info("State store: " + stateStore.name());
        /*
        groupedRelevantG2BookingStream
                .toStream()
                // filter is used for logging
                .filter(
                      (kopfnummer, count) -> {
                          LOG.info(
                                  String.format(
                                          "KTable groupedRelevantG2BookingStream: (%s, %d)",
                                          kopfnummer,
                                          count
                                  )
                          );
                          return true;
                      }
                );
         */

        //HostInfo hostInfo = interactiveQueryService.getCurrentHostInfo();
        //LOG.info("Host: " + hostInfo);


        //kafkaStreamsRegistry.getKafkaStreams().forEach((k) -> {
        //    KeyQueryMetadata keyQueryMetadata = k.queryMetadataForKey(store, key, serializer);
        //    if (keyQueryMetadata != null) {
        //        kafkaStreamsAtomicReference.set(k);
        //    }
        //
        //});

        //KafkaStreams kafkaStreams = oldG2BookingStreamBuilderFactory.getKafkaStreams();
        //System.out.println("State: " + kafkaStreams.state());
        //kafkaStreams.store("newG2BookingsCounterStore", QueryableStoreTypes.keyValueStore());

        //ReadOnlyKeyValueStore<String, Long> keyValueStore =
        ReadOnlyKeyValueStore<Object, Object> keyValueStore =
                interactiveQueryService.getQueryableStore(
                        "newG2BookingsCounterStore",
                        QueryableStoreTypes.keyValueStore()
                );

        //LOG.info(String.format("%s", keyValueStore.get("2020")));

        for (KeyValueIterator<Object, Object> it = keyValueStore.all(); it.hasNext(); ) {
            KeyValue<Object, Object> entry = it.next();
            LOG.info(String.format("%s: %s", entry.key, entry.value));
        }

        /*
        // Get the key-value store CountsKeyValueStore
        ReadOnlyKeyValueStore<String, Long> keyValueStore =
                streams.store("CountsKeyValueStore", QueryableStoreTypes.keyValueStore());

        // Get value by key
        System.out.println("count for hello:" + keyValueStore.get("hello"));

        // Get the values for a range of keys available in this application instance
        KeyValueIterator<String, Long> range = keyValueStore.range("all", "streams");
        while (range.hasNext()) {
            KeyValue<String, Long> next = range.next();
            System.out.println("count for " + next.key + ": " + next.value);
        }
        // close the iterator to release resources
        range.close();
         */
    }

}
