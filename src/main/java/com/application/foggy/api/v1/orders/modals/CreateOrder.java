package com.application.foggy.api.v1.orders.modals;

import com.application.foggy.api.v1.customers.Customer;
import com.application.foggy.api.v1.orders.OrderLines;
import lombok.Data;

import java.util.List;
@Data
public class CreateOrder {
    private Customer customer;
    private List<OrderLines> orderLines;
}
