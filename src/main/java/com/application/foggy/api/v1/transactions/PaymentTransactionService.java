package com.application.foggy.api.v1.transactions;

import com.application.foggy.api.v1.orders.Order;
import com.application.foggy.api.v1.orders.OrdersRepository;
import com.application.foggy.documentnumbering.DocumentNumberingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PaymentTransactionService {

    private PaymentTransactionRepository repository;
    private DocumentNumberingService documentNumberingService;
    private OrdersRepository ordersRepository;

    public PaymentTransaction createTransaction(PaymentTransaction transaction) {
        transaction.setId(documentNumberingService.getDocumentNumber(DocumentNumberingService.TRANSACTION));
        transaction.setCreatedOn(LocalDateTime.now());
        transaction.setCreatedBy("username");
        PaymentTransaction paymentTransaction = repository.save(transaction);
        documentNumberingService.increment(DocumentNumberingService.TRANSACTION);
        return paymentTransaction;
    }

    public List<PaymentTransaction> getTransactionsByCustomerId(String customerId) {
        return ordersRepository.findAllByCustomerId(customerId).stream()
                .map(Order::getPaymentTransaction)
                .flatMap(List::stream)
                .sorted((o1, o2) -> o2.getCreatedOn().compareTo(o1.getCreatedOn()))
                .toList();
    }

    public List<PaymentTransaction> getAllTransactions() {
        return repository.findAll().stream()
                .sorted((o1, o2) -> o2.getCreatedOn().compareTo(o1.getCreatedOn()))
                .toList();
    }
}
