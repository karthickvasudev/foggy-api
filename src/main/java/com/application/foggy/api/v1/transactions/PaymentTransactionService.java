package com.application.foggy.api.v1.transactions;

import com.application.foggy.documentnumbering.DocumentNumberingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PaymentTransactionService {

    private PaymentTransactionRepository repository;
    private DocumentNumberingService documentNumberingService;

    public PaymentTransaction createTransaction(PaymentTransaction transaction) {
        transaction.setId(documentNumberingService.getDocumentNumber(DocumentNumberingService.TRANSACTION));
        transaction.setCreatedOn(LocalDateTime.now());
        transaction.setCreatedBy("username");
        PaymentTransaction paymentTransaction = repository.save(transaction);
        documentNumberingService.increment(DocumentNumberingService.TRANSACTION);
        return paymentTransaction;
    }
}
