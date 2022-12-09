package com.application.foggy.api.v1.customers;

import com.application.foggy.documentnumbering.DocumentNumberingService;
import com.application.foggy.support.exceptionresponse.ErrorResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomersService {
    private final CustomersRepository repository;
    private final DocumentNumberingService documentNumberingService;

    public Customer createCustomer(Customer customer) {
        boolean flag = repository.findAll().stream().anyMatch(c -> c.getPhoneNumber().equals(customer.getPhoneNumber()));
        if (flag) {
            return repository.findByPhoneNumber(customer.getPhoneNumber()).get();
        }
        customer.setId(documentNumberingService.getDocumentNumber(DocumentNumberingService.CUSTOMER));
        documentNumberingService.increment(DocumentNumberingService.CUSTOMER);
        return repository.save(customer);
    }

    public Customer getCustomer(String id) {
        Optional<Customer> customer = repository.findById(id);
        if (customer.isPresent()) {
            return customer.get();
        }
        throw new ErrorResponse(HttpStatus.NOT_FOUND, String.format("%s - customer not found", id));
    }

    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    public Customer getCustomerByPhoneNumber(String phoneNumber) {
        Optional<Customer> customer = repository.findByPhoneNumber(phoneNumber);
        if(customer.isPresent()){
            return customer.get();
        }
        throw new ErrorResponse(HttpStatus.NOT_FOUND,String.format("No customer available with %s",phoneNumber));
    }
}
