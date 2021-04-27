package de.helaba.jets.g2.domain.repository;

import de.helaba.jets.g2.domain.InboundFile;
import de.helaba.jets.g2.domain.Pacs008PaymentTransaction;
import de.helaba.jets.g2.domain.ServiceType;
import java.math.BigDecimal;
import java.util.Date;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class Pacs008PaymentTransactionRepositoryTest extends AbstractJpaRepositoryFactorySetup{

    @Autowired
    private Pacs008PaymentTransactionRepository repository;


    @Test
    public void testInsert() {
        InboundFile inboundFile =  new InboundFile();
        inboundFile.setFilename("testFilename");
        inboundFile.setCreationTimestamp(new Date());
        inboundFile.setRemitter("TestRemitter");

        getEntityManager().getTransaction().begin();
        Pacs008PaymentTransaction pacs008PaymentTransaction = new Pacs008PaymentTransaction();
        pacs008PaymentTransaction.setAmount(new BigDecimal("10.00"));
        pacs008PaymentTransaction.setRemittanceInformation("Remittance information");
        pacs008PaymentTransaction.setServiceType(ServiceType.SCC);
        pacs008PaymentTransaction.setSettlementDate(new Date());
        pacs008PaymentTransaction.setInboundFile(inboundFile);

        repository.save(pacs008PaymentTransaction);

        getEntityManager().getTransaction().commit();
        assertThat(repository.findById(pacs008PaymentTransaction.getUid())).hasValue(pacs008PaymentTransaction);

    }
}
