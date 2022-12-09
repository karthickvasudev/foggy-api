package com.application.foggy.api.v1.customers;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomersRepository extends MongoRepository<Customer, String> {
    Optional<Customer> findByPhoneNumber(String phoneNumber);
}
