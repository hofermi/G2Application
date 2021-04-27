package de.helaba.jets.g2.domain.repository;

import de.helaba.jets.g2.domain.InboundFile;
import de.helaba.jets.g2.domain.Pacs008PaymentTransaction;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface InboundFileRepository extends CrudRepository<InboundFile, Long> {

    InboundFile findByUid(long uid);

}

