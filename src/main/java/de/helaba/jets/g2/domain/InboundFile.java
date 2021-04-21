package de.helaba.jets.g2.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Entity
public class InboundFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
