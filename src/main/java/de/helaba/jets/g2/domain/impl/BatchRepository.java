package de.helaba.jets.g2.domain.impl;

import de.helaba.jets.g2.ParentListPair;
import de.helaba.jets.g2.domain.AbstractEntity;
import de.helaba.jets.g2.domain.InboundFile;
import de.helaba.jets.g2.domain.Pacs008PaymentTransaction;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

 @NoRepositoryBean
public interface BatchRepository <T extends AbstractEntity, ID extends Serializable> extends JpaRepository<T, ID> {
    public <S extends T> void saveInBatch(Iterable<S> entities);

}
