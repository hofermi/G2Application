package de.helaba.jets.g2.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Entity
@Table(name = "PACS008_PAYMENT_TRANSACTION")
public class LoaderPacs008PaymentTransaction extends AbstractEntity {

    @NotNull
    private ServiceType serviceType;

    @NotNull
    private BigDecimal amount;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date settlementDate;

    @Size(max = 1024, message = "Remittance Information cannot be longer than 1024 characters")
    private String remittanceInformation;


    @NotNull()
    @Column(name = "inputFile_id")
    private Long idInputFile;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @PrePersist
    void createdAt() {
        creationDate = new Date();
    }


}


