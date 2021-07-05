package de.helaba.jets.g2.domain.repository;

import de.helaba.jets.g2.domain.InboundFile;
import de.helaba.jets.g2.domain.Pacs008PaymentTransaction;
import de.helaba.jets.g2.domain.impl.BatchRepository;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Pacs008PaymentTransactionRepository extends BatchRepository<Pacs008PaymentTransaction, Long> {

    Pacs008PaymentTransaction findByUid(long uid);

    List<Pacs008PaymentTransaction> findByInboundFile(InboundFile inboundFile);

    List<Pacs008PaymentTransaction> findByInboundFileUid(long uid);

}

