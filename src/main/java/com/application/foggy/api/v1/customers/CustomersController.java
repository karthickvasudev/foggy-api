package com.application.foggy.api.v1.customers;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/customers")
@AllArgsConstructor
public class CustomersController {
    private final CustomersService service;

    @PostMapping("create")
    public Customer create(@RequestBody Customer customer) {
        return service.createCustomer(customer);
    }

    @GetMapping("{id}")
    public Customer get(@PathVariable String id) {
        return service.getCustomer(id);
    }

    @GetMapping()
    public List<Customer> list() {
        return service.getAllCustomers();
    }

    @GetMapping("{phoneNumber}/phoneNumber")
    public Customer phoneNumber(@PathVariable String phoneNumber) {
        return service.getCustomerByPhoneNumber(phoneNumber);
    }

    @PutMapping("{id}")
    public Customer updateCustomer(@PathVariable String id, @RequestBody Customer customer) {
        return service.updateCustomer(id, customer);
    }
}
