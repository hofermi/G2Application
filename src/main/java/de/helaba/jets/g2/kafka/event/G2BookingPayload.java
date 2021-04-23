package de.helaba.jets.g2.kafka.event;

import de.helaba.jets.g2.kafka.avro.model.G2BookingAvroRecord;

public class G2BookingPayload {

    private String creditorIban;
    private String debtorIban;
    private String amount;

    public G2BookingPayload() {
    }

    public G2BookingPayload(String creditorIban, String debtorIban, String amount) {
        this.creditorIban = creditorIban;
        this.debtorIban = debtorIban;
        this.amount = amount;
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

    @Override
    public String toString() {
        return
                "G2BookingPayload{" +
                "creditorIban=" + creditorIban +
                ", debtorIban=" + debtorIban +
                ", amount=" + amount +
                '}';
    }

    public G2BookingAvroRecord toAvroRecord() {
        return
                G2BookingAvroRecord
                        .newBuilder()
                        .setCreditorIban(creditorIban)
                        .setDebtorIban(debtorIban)
                        .setAmount(amount)
                        .build();
    }

    public static G2BookingPayload fromAvroRecord(G2BookingAvroRecord record) {
        return
                new G2BookingPayload(
                        String.valueOf(record.getCreditorIban()),
                        String.valueOf(record.getDebtorIban()),
                        String.valueOf(record.getAmount())
                );
    }

}
