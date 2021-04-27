package de.helaba.jets.g2.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
//@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Entity
@Table(name = "PACS008_PAYMENT_TRANSACTION")
public class Pacs008PaymentTransaction implements Serializable {

    @Id
    @SequenceGenerator(name = "jpaPkSeq", sequenceName = "JPA_PK_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jpaPkSeq")
    @Column(name = "PK_ID")
    private Long uid;

    @NotNull
    private ServiceType serviceType;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Date settlementDate;

    @Size(max = 1024, message = "Remittance Information cannot be longer than 1024 characters")
    private String remittanceInformation;

    @NotNull
    @ManyToOne
    private InboundFile inboundFile;

}


