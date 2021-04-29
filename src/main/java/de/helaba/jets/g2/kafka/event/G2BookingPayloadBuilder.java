package de.helaba.jets.g2.kafka.event;

import java.time.LocalDate;
import java.util.Date;

public class G2BookingPayloadBuilder {

    private G2BookingPayload result;

    public G2BookingPayloadBuilder() {
        result = new G2BookingPayload();
    }

    public G2BookingPayloadBuilder withKopfnummer(String value) {
        result.setKopfnummer(value);
        return this;
    }

    public G2BookingPayloadBuilder withMessageType(MessageType value) {
        result.setMessageType(value);
        return this;
    }

    public G2BookingPayloadBuilder withCreditorIban(String value) {
        result.setCreditorIban(value);
        return this;
    }

    public G2BookingPayloadBuilder withDebtorIban(String value) {
        result.setDebtorIban(value);
        return this;
    }

    public G2BookingPayloadBuilder withAmount(String value) {
        result.setAmount(value);
        return this;
    }

    public G2BookingPayloadBuilder withBatchBooking(boolean value) {
        result.setBatchBooking(value);
        return this;
    }

    public G2BookingPayloadBuilder withNoOfBatchBookingTransactions(long value) {
        result.setNoOfBatchBookingTransactions(value);
        return this;
    }

    public G2BookingPayloadBuilder withBookingDate(LocalDate value) {
        result.setBookingDate(value);
        return this;
    }

    public G2BookingPayloadBuilder withValuta(LocalDate value) {
        result.setValuta(value);
        return this;
    }

    public G2BookingPayload build() {
        return result;
    }

}
