package de.helaba.jets.g2.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
//  @RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Entity
@Table(name = "INBOUND_FILE")
public class InboundFile extends AbstractEntity {

    @NotNull
    @Size(min = 5, message = "Filename must be at least 5 characters long")
    private String filename;

    @NotNull
    @Size(min = 5, message = "Filename must be at least 5 characters long")
    private String remitter;

    private Date creationTimestamp;

    @PrePersist
    void createdAt() {
        creationTimestamp = new Date();
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inboundFile", orphanRemoval = true)
    private List<Pacs008PaymentTransaction> transactions = new ArrayList<>();

}
