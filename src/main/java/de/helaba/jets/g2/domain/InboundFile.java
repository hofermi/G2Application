package de.helaba.jets.g2.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
// @RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Entity
@Table(name = "INBOUND_FILE")
public class InboundFile implements Serializable {

    @Id
    @SequenceGenerator(name = "jpaPkSeq", sequenceName = "JPA_PK_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jpaPkSeq")
    @Column(name = "PK_ID")
    private Long uid;

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

}
