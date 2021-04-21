package de.helaba.jets.g2.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class Pacs008PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private ServiceType serviceType;

    @NotNull()
    private BigDecimal amount;

    @NotNull
    private Date settlementDate;

    @Size(max = 1024, message = "Remittance Information cannot be longer than 1024 characters")
    private String remittanceInformation;

    @NotNull
    @ManyToOne
    private InboundFile inboundFile;

}


