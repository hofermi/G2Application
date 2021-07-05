package de.helaba.jets.g2.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class AbstractEntity<P extends AbstractEntity> implements Serializable {
    @Id
    // @SequenceGenerator(name = "jpaPkSeq", sequenceName = "JPA_PK_SEQ", allocationSize = 1, initialValue = 1)
    // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jpaPkSeq")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PK_ID")
    protected Long uid;

    // TODO
    // /equals()
    // toString()
    //hashCode()
}
