package com.application.foggy.api.v1.orders;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends MongoRepository<Order, String> {
    List<Order> findAllByCustomerId(String customerId);

    Optional<Order> findByBill(String bill);
}
