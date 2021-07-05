package de.helaba.jets.g2.domain.impl;

import static org.springframework.transaction.annotation.Propagation.NEVER;

import de.helaba.jets.g2.domain.AbstractEntity;
import de.helaba.jets.g2.domain.InboundFile;
import de.helaba.jets.g2.domain.repository.SpringContext;
import java.io.Serializable;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = NEVER)
public class BatchRepositoryImpl<T extends AbstractEntity, ID extends Serializable>
        extends SimpleJpaRepository<T, ID> implements BatchRepository<T, ID> {

    private final EntityManager entityManager;

    public BatchRepositoryImpl(
            JpaEntityInformation<T, ?> entityInformation,
            EntityManager entityManager
    ) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Transactional(isolation = Isolation.DEFAULT)
    @Override
    public <S extends T> void saveInBatch(Iterable<S> entities) {

        if (entities == null) {
            throw new IllegalArgumentException("The given Iterable of entities cannot be null!");
        }

        BatchExecutor batchExecutor = SpringContext.getBean(BatchExecutor.class);
        batchExecutor.saveInBatch(entities);
    }

}

