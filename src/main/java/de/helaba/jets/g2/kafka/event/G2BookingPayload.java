package de.helaba.jets.g2.kafka.event;

import java.time.LocalDate;

public class G2BookingPayload {

    private String kopfnummer;
    private MessageType messageType;
    private String creditorIban;
    private String debtorIban;
    private String amount;
    private boolean isBatchBooking;
    private long noOfBatchBookingTransactions;
    private LocalDate bookingDate;
    private LocalDate valuta;

    public G2BookingPayload() {
    }

    public String getKopfnummer() {
        return kopfnummer;
    }
    public void setKopfnummer(String kopfnummer) {
        this.kopfnummer = kopfnummer;
    }

    public MessageType getMessageType() {
        return messageType;
    }
    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getCreditorIban() {
        return creditorIban;
    }
    public void setCreditorIban(String creditorIban) {
        this.creditorIban = creditorIban;
    }

    public String getDebtorIban() {
        return debtorIban;
    }
    public void setDebtorIban(String debtorIban) {
        this.debtorIban = debtorIban;
    }

    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean isBatchBooking() {
        return isBatchBooking;
    }
    public void setBatchBooking(boolean batchBooking) {
        isBatchBooking = batchBooking;
    }

    public long getNoOfBatchBookingTransactions() {
        return noOfBatchBookingTransactions;
    }
    public void setNoOfBatchBookingTransactions(long noOfBatchBookingTransactions) {
        this.noOfBatchBookingTransactions = noOfBatchBookingTransactions;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }
    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDate getValuta() {
        return valuta;
    }
    public void setValuta(LocalDate valuta) {
        this.valuta = valuta;
    }

    @Override public String toString() {
        return "G2BookingPayload{" +
                "kopfnummer='" + kopfnummer + '\'' +
                ", messageType=" + messageType +
                ", creditorIban='" + creditorIban + '\'' +
                ", debtorIban='" + debtorIban + '\'' +
                ", amount='" + amount + '\'' +
                ", isBatchBooking=" + isBatchBooking +
                ", noOfBatchBookingTransactions=" + noOfBatchBookingTransactions +
                ", bookingDate=" + bookingDate +
                ", valuta=" + valuta +
                '}';
    }

}
