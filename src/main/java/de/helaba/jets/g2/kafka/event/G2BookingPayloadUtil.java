package de.helaba.jets.g2.kafka.event;

import de.helaba.jets.g2.kafka.avro.model.AvroG2BookingRecord;
import de.helaba.jets.g2.kafka.avro.model.AvroMessageType;
import java.time.LocalDate;
import java.util.Random;
import org.apache.commons.lang3.RandomUtils;

public class G2BookingPayloadUtil {

    private final static String[] KOPFNUMMERN = {"2020", "2031", "2044", "2045", "2048"};
    private final static MessageType[] MESSAGE_TYPES = {
            MessageType.PACS002,
            MessageType.PACS003,
            MessageType.PACS004,
            MessageType.PACS007,
            MessageType.PACS008,
            MessageType.CAMT056
    };
    private final static String[] CREDITOR_IBANS = {
            "DE85500105171137392923",
            "DE67500105174395571158",
            "DE70500105178988644294",
            "DE55500105178471587325",
            "DE36500105172158359836",
            "DE07500105172917841258",
            "DE12500105174178549489",
            "DE39500105177856524969",
            "DE36500105174522167677",
            "DE18500105174158866141"
    };
    private final static String[] DEBTOR_IBANS = {
            "DE14500105173828811297",
            "DE72500105178549943425",
            "DE26500105176187767273",
            "DE82500105176683648889",
            "DE78500105177737529323",
            "DE96500105175294512219",
            "DE48500105174692944982",
            "DE16500105177111355863",
            "DE49500105176517656681",
            "DE63500105177556571922"
    };
    private final static LocalDate[] BOOKING_DATES = {
            LocalDate.of(2021, 3, 19),
            LocalDate.of(2021, 3, 20),
            LocalDate.of(2021, 3, 21)
    };
    private final static LocalDate[] VALUTAS = {
            LocalDate.of(2021, 3, 21),
            LocalDate.of(2021, 3, 22),
            LocalDate.of(2021, 3, 23)
    };

    public static AvroG2BookingRecord toAvroRecord(G2BookingPayload payload) {
        return
                AvroG2BookingRecord
                        .newBuilder()
                        .setKopfnummer(payload.getKopfnummer())
                        .setMessageType(AvroMessageType.valueOf(payload.getMessageType().name()))
                        .setCreditorIban(payload.getCreditorIban())
                        .setDebtorIban(payload.getDebtorIban())
                        .setAmount(payload.getAmount())
                        .setIsBatchBooking(payload.isBatchBooking())
                        .setNoOfBatchBookingTransactions(payload.getNoOfBatchBookingTransactions())
                        .setBookingDate(payload.getBookingDate())
                        .setValuta(payload.getValuta())
                        .build();
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

    public static G2BookingPayload random() {
        Random random = new Random();
        boolean isBatchBooking = new Random().nextBoolean();
        return
                new G2BookingPayloadBuilder()
                        .withKopfnummer(KOPFNUMMERN[random.nextInt(KOPFNUMMERN.length - 1)])
                        .withMessageType(MESSAGE_TYPES[random.nextInt(MESSAGE_TYPES.length - 1)])
                        .withCreditorIban(CREDITOR_IBANS[random.nextInt(CREDITOR_IBANS.length - 1)])
                        .withDebtorIban(DEBTOR_IBANS[random.nextInt(DEBTOR_IBANS.length - 1)])
                        .withAmount(String.format("%.2f", RandomUtils.nextFloat(0.01f, 9999.99f)))
                        .withBatchBooking(isBatchBooking)
                        .withNoOfBatchBookingTransactions(isBatchBooking ? 1 + random.nextInt(500) : 0)
                        .withBookingDate(BOOKING_DATES[random.nextInt(BOOKING_DATES.length - 1)])
                        .withValuta(VALUTAS[random.nextInt(VALUTAS.length - 1)])
                        .build();
    }

}
