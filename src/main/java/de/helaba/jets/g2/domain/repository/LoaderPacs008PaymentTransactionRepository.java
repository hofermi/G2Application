package de.helaba.jets.g2.domain.repository;

import de.helaba.jets.g2.domain.InboundFile;
import de.helaba.jets.g2.domain.LoaderPacs008PaymentTransaction;
import de.helaba.jets.g2.domain.Pacs008PaymentTransaction;
import de.helaba.jets.g2.domain.impl.BatchRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface LoaderPacs008PaymentTransactionRepository extends BatchRepository<LoaderPacs008PaymentTransaction, Long> {

}

