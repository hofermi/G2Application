package de.helaba.jets.g2.kafka.producer;

import de.helaba.jets.g2.kafka.event.G2BookingPayload;
import org.springframework.context.ApplicationEvent;

public class G2BookingPayloadSentEvent extends ApplicationEvent {

    private G2BookingPayload g2BookingPayload;

    public G2BookingPayloadSentEvent(Object source, G2BookingPayload g2BookingPayload) {
        super(source);
        this.g2BookingPayload = g2BookingPayload;
    }

    public G2BookingPayload getG2BookingPayload() {
        return g2BookingPayload;
    }

}
