package de.helaba.jets.g2;

import de.helaba.jets.g2.domain.InboundFile;
import de.helaba.jets.g2.domain.LoaderInputFile;
import de.helaba.jets.g2.domain.LoaderPacs008PaymentTransaction;
import de.helaba.jets.g2.domain.impl.BatchRepositoryImpl;
import de.helaba.jets.g2.domain.service.InboundFileService;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BatchRepositoryImpl.class)
public class G2Application implements CommandLineRunner {

   @Autowired
   private InboundFileService inboundFileService;

    private static final Logger log = LoggerFactory.getLogger(G2Application.class);

    public static void main(String[] args) {
        SpringApplication.run(G2Application.class, args);

    }


    @Override
    public void run(String... args) {
        DomainObjectGenerator domainObjectGenerator = new DomainObjectGenerator();
/*        log.info("Starting ObjectGenerator");

        List<InboundFile> files = domainObjectGenerator.startGenerator(5, 10000, new Date(), new Date());
        log.info("Stopping ObjectGenerator");

        log.info("Starting to store data with ordering");
        long startTime = new Date().getTime();
        inboundFileService.batchInboundFileAndTransactions(files);
        long stopTime = new Date().getTime();
        log.info("Stopping to store data with ordering");

        double resultTime = (stopTime - startTime) / 1000.0;

        log.info(String.format("Duration: %f", resultTime));
*/        log.info("Starting ObjectGenerator for separate entities");
        List<InboundFile> files = domainObjectGenerator.startGenerator(5, 10000, new Date(), new Date());
        log.info("Stopping ObjectGenerator for separate entities");

        log.info("Starting to store data via Batching (Saving parent object in a separate transaction");
        long startTime = new Date().getTime();
        inboundFileService.batchInboundFileAndTransactions(files);
        long stopTime = new Date().getTime();
        log.info("Stopping to store data via Batching (Saving parent object in a separate transaction");
        double resultTime = (stopTime - startTime) / 1000.0;

        log.info(String.format("Duration: %f", resultTime));

    }


}
