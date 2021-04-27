package de.helaba.jets.g2.domain.repository;

import de.helaba.jets.g2.domain.InboundFile;
import de.helaba.jets.g2.domain.Pacs008PaymentTransaction;
import de.helaba.jets.g2.domain.ServiceType;
import java.math.BigDecimal;
import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class Pacs008PaymentTransactionRepositoryTest extends AbstractJpaRepositoryFactorySetup {

    @Autowired
    private Pacs008PaymentTransactionRepository pacs008Repository;

    @Autowired
    private InboundFileRepository inboundFileRepository;

    @Test
    public void testInsert() {
        //getEntityManager().getTransaction().begin();

        InboundFile inboundFile =  new InboundFile();
        inboundFile.setFilename("testFilename");
        inboundFile.setCreationTimestamp(new Date());
        inboundFile.setRemitter("TestRemitter");
        inboundFileRepository.save(inboundFile);

        Pacs008PaymentTransaction pacs008PaymentTransaction = new Pacs008PaymentTransaction();
        pacs008PaymentTransaction.setAmount(new BigDecimal("10.00"));
        pacs008PaymentTransaction.setRemittanceInformation("Remittance information");
        pacs008PaymentTransaction.setServiceType(ServiceType.SCC);
        pacs008PaymentTransaction.setSettlementDate(new Date());
        pacs008PaymentTransaction.setInboundFile(inboundFile);
        Pacs008PaymentTransaction entity = pacs008Repository.save(pacs008PaymentTransaction);

        //getEntityManager().getTransaction().commit();

        //Pacs008PaymentTransaction entity = pacs008Repository.findByUid(pacs008PaymentTransaction.getUid());
        Assertions.assertNotNull(entity);
        Assertions.assertEquals(pacs008PaymentTransaction, entity);
    }

}
