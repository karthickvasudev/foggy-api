package com.application.foggy.api.v1.transactions;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/transactions")
@AllArgsConstructor
public class PaymentTransactionController {
    private PaymentTransactionService service;

    @GetMapping("{customerId}")
    public List<PaymentTransaction> getTransactionsByCustomerId(@PathVariable String customerId) {
        return service.getTransactionsByCustomerId(customerId);
    }

    @GetMapping()
    public List<PaymentTransaction> getTransactions(){
        return service.getAllTransactions();
    }
}
