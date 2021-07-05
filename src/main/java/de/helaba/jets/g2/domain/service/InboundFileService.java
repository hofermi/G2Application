package de.helaba.jets.g2.domain.service;

import de.helaba.jets.g2.ParentListPair;
import de.helaba.jets.g2.domain.InboundFile;
import de.helaba.jets.g2.domain.LoaderInputFile;
import de.helaba.jets.g2.domain.LoaderPacs008PaymentTransaction;
import de.helaba.jets.g2.domain.Pacs008PaymentTransaction;
import de.helaba.jets.g2.domain.repository.InboundFileRepository;
import de.helaba.jets.g2.domain.repository.LoaderInputFileRepository;
import de.helaba.jets.g2.domain.repository.LoaderPacs008PaymentTransactionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class InboundFileService {

    private final InboundFileRepository inboundFileRepository;

    private final LoaderInputFileRepository loaderInputFileRepository;

    private final LoaderPacs008PaymentTransactionRepository loaderPacs008PaymentTransactionRepository;

    public InboundFileService(InboundFileRepository inboundFileRepository, LoaderInputFileRepository loaderInputFileRepository, LoaderPacs008PaymentTransactionRepository loaderPacs008PaymentTransactionRepository) {
        this.inboundFileRepository = inboundFileRepository;
        this.loaderInputFileRepository = loaderInputFileRepository;
        this.loaderPacs008PaymentTransactionRepository = loaderPacs008PaymentTransactionRepository;
    }

    public void batchInboundFileAndTransactions(List<InboundFile> inboundFiles){
        inboundFileRepository.saveInBatch(inboundFiles);
    }

    public LoaderInputFile saveLoaderInputFile(LoaderInputFile loaderInputFile) {
        return loaderInputFileRepository.save(loaderInputFile);
    }

    public void saveLoaderPacs008Transaction(Long parentId, List<LoaderPacs008PaymentTransaction> transactions) {
        for (LoaderPacs008PaymentTransaction tx : transactions) {
            tx.setIdInputFile(parentId);
        }
        loaderPacs008PaymentTransactionRepository.saveInBatch(transactions);
    }

}
