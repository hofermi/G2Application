package de.helaba.jets.domain.repository;

import de.helaba.jets.domain.InboundFile;
import de.helaba.jets.domain.Pacs008PaymentTransaction;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface Pacs008PaymentTransactionRepository extends CrudRepository<Pacs008PaymentTransaction, Long> {

    Pacs008PaymentTransaction findById(long id);

    List<Pacs008PaymentTransaction> findByInboundFile(InboundFile inboundFile);

    List<Pacs008PaymentTransaction> findByInboundFileId(long id);
}

