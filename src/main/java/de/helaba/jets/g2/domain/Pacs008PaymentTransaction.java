package de.helaba.jets.g2.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
//@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Entity
@Table(name = "PACS008_PAYMENT_TRANSACTION")
public class Pacs008PaymentTransaction extends AbstractEntity {

    @NotNull
    private ServiceType serviceType;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date settlementDate;

    @Size(max = 1024, message = "Remittance Information cannot be longer than 1024 characters")
    private String remittanceInformation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inputFile_id")
    private InboundFile inboundFile;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @PrePersist
    void createdAt() {
        creationDate = new Date();
    }


}


