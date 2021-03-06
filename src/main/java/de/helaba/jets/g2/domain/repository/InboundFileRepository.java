package de.helaba.jets.g2.domain.repository;

import de.helaba.jets.g2.domain.InboundFile;
import de.helaba.jets.g2.domain.LoaderInputFile;
import de.helaba.jets.g2.domain.Pacs008PaymentTransaction;
import de.helaba.jets.g2.domain.impl.BatchRepository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InboundFileRepository extends JpaRepository<LoaderInputFile, Long> {
    void saveInBatch(Iterable inboundFile);
}
