package de.helaba.jets.g2.domain.repository;

import de.helaba.jets.g2.domain.InboundFile;
import de.helaba.jets.g2.domain.Pacs008PaymentTransaction;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface Pacs008PaymentTransactionRepository extends CrudRepository<Pacs008PaymentTransaction, Long> {

    Pacs008PaymentTransaction findByUid(long uid);

    List<Pacs008PaymentTransaction> findByInboundFile(InboundFile inboundFile);

    List<Pacs008PaymentTransaction> findByInboundFileUid(long uid);

}

