package com.application.foggy.api.v1.customers;

import com.application.foggy.api.v1.orders.Order;
import com.application.foggy.api.v1.orders.OrdersRepository;
import com.application.foggy.api.v1.orders.enums.ORDERSTATUS;
import com.application.foggy.documentnumbering.DocumentNumberingService;
import com.application.foggy.support.exceptionresponse.ErrorResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomersService {
    private final CustomersRepository repository;
    private final DocumentNumberingService documentNumberingService;
    private final OrdersRepository ordersRepository;

    public Customer createCustomer(Customer customer) {
        boolean flag = repository.findAll().stream().anyMatch(c -> c.getPhoneNumber().equals(customer.getPhoneNumber()));
        if (flag) {
            return repository.findByPhoneNumber(customer.getPhoneNumber()).get();
        }
        customer.setId(documentNumberingService.getDocumentNumber(DocumentNumberingService.CUSTOMER));
        customer.setCreatedOn(LocalDateTime.now());
        customer.setCreatedBy("user name");
        Customer saveCustomer = repository.save(customer);
        documentNumberingService.increment(DocumentNumberingService.CUSTOMER);
        return saveCustomer;
    }

    public Customer getCustomer(String id) {
        Optional<Customer> customerOpt = repository.findById(id);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            List<Order> orders = ordersRepository.findAllByCustomerId(customer.getId()).stream()
                    .filter(order -> order.getStatus().equals(ORDERSTATUS.DELIVERED)).collect(Collectors.toList());
            customer.setPendingAmount(orders.stream().mapToDouble(order -> order.getOrderPaymentDetails().getBalance()).sum());
            customer.setAdvanceAmount(orders.stream().mapToDouble(order -> order.getOrderPaymentDetails().getAdvance()).sum());
            return customer;
        }
        throw new ErrorResponse(HttpStatus.NOT_FOUND, String.format("%s - customer not found", id));
    }

    public List<Customer> getAllCustomers() {
        return repository.findAll().stream().map((customer) -> getCustomer(customer.getId())).collect(Collectors.toList());
    }

    public Customer getCustomerByPhoneNumber(String phoneNumber) {
        Optional<Customer> customer = repository.findByPhoneNumber(phoneNumber);
        if (customer.isPresent()) {
            return customer.get();
        }
        throw new ErrorResponse(HttpStatus.NOT_FOUND, String.format("No customer available with %s", phoneNumber));
    }

    public Customer updateCustomer(String id, Customer customer) {
        Customer c = getCustomer(id);
        c.setName(customer.getName());
        c.setPhoneNumber(customer.getPhoneNumber());
        c.setAddress(customer.getAddress());
        c.setUpdatedBy("updated user name");
        c.setUpdatedOn(LocalDateTime.now());
        return repository.save(customer);
    }
}
