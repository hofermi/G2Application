package de.helaba.jets.g2;

import de.helaba.jets.g2.domain.InboundFile;
import de.helaba.jets.g2.domain.LoaderInputFile;
import de.helaba.jets.g2.domain.LoaderPacs008PaymentTransaction;
import de.helaba.jets.g2.domain.Pacs008PaymentTransaction;
import de.helaba.jets.g2.domain.ServiceType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javassist.Loader;
import javax.validation.constraints.Max;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class DomainObjectGenerator {



    public List<InboundFile> startGenerator(int numberOfFiles, int numberOfTransactions, Date creationDate, Date settlementDate) {
        List<InboundFile> result = new ArrayList<>();
        creationDate = creationDate != null ? creationDate : new Date();
        settlementDate = settlementDate != null ? settlementDate : new Date();
        for (int fileCounter = 0; fileCounter < numberOfFiles; fileCounter++) {
            InboundFile inboundFile = createInboundFile(creationDate, fileCounter);
            List <Pacs008PaymentTransaction> transactions = createPacs008Transaction(inboundFile, numberOfTransactions, creationDate, settlementDate);
            inboundFile.setTransactions(transactions);
            result.add(inboundFile);
        }
        return result;
    }
    private List<Pacs008PaymentTransaction> createPacs008Transaction(InboundFile inboundFile, int numberOfTransactions, Date creationDate, Date settlementDate) {
        List<Pacs008PaymentTransaction> result = new ArrayList<>(numberOfTransactions);
        for (int txCounter = 0; txCounter < numberOfTransactions; txCounter++) {
            Pacs008PaymentTransaction tx  = new Pacs008PaymentTransaction();
            tx.setSettlementDate(creationDate);
            tx.setServiceType(ServiceType.SCT);
            tx.setAmount(new BigDecimal(txCounter));
            tx.setRemittanceInformation(String.format("Remittance information of TX %d ", txCounter));
            tx.setSettlementDate(settlementDate);
            result.add(tx);
            tx.setInboundFile(inboundFile);
        }
        return  result;
    }


    private InboundFile createInboundFile(Date creationDate, int fileCounter, List<Pacs008PaymentTransaction> transactions) {
        InboundFile inboundFile = createInboundFile(creationDate, fileCounter);
        inboundFile.setTransactions(transactions);
        return  inboundFile;
    }

    private InboundFile createInboundFile(Date creationDate, int fileCounter) {
        InboundFile inboundFile = new InboundFile();
        inboundFile.setFilename(String.format("Filename %d", fileCounter ));
        inboundFile.setCreationTimestamp(creationDate);
        inboundFile.setRemitter(String.format("Remitter %d", fileCounter));
        return  inboundFile;
    }

    private LoaderInputFile createLoaderInputFile(Date creationDate, int fileCounter) {
        LoaderInputFile inboundFile = new LoaderInputFile();
        inboundFile.setFilename(String.format("Filename %d", fileCounter ));
        inboundFile.setCreationTimestamp(creationDate);
        inboundFile.setRemitter(String.format("Remitter %d", fileCounter));
        return  inboundFile;
    }


    public List<ParentListPair<LoaderInputFile, List<LoaderPacs008PaymentTransaction>>> startGeneratorWithSeparateParent(int numberOfTransactions,
                                                                                                                     Date creationDate,
                                                                                                                     Date settlementDate,
                                                                                                                     int numberOfFiles) {
        List<ParentListPair<LoaderInputFile, List<LoaderPacs008PaymentTransaction>>> result = new ArrayList<>();
        for (int fileCounter = 0; fileCounter < numberOfFiles; fileCounter++) {
            LoaderInputFile inboundFile = createLoaderInputFile(creationDate, fileCounter);
            result.add(new ParentListPair<>(
                    inboundFile,
                    createLoaderPacs008Transaction(
                            numberOfTransactions,
                            creationDate,
                            settlementDate
                    )
            ));
        }
        return result;
    }
    private List<LoaderPacs008PaymentTransaction> createLoaderPacs008Transaction( int numberOfTransactions, Date creationDate, Date settlementDate) {
        List<LoaderPacs008PaymentTransaction> result = new ArrayList<>(numberOfTransactions);
        for (int txCounter = 0; txCounter < numberOfTransactions; txCounter++) {
            LoaderPacs008PaymentTransaction tx  = new LoaderPacs008PaymentTransaction();
            tx.setSettlementDate(creationDate);
            tx.setServiceType(ServiceType.SCT);
            tx.setAmount(new BigDecimal(txCounter));
            tx.setRemittanceInformation(String.format("Remittance information of TX %d ", txCounter));
            tx.setSettlementDate(settlementDate);
            result.add(tx);
        }
        return  result;
    }

}
