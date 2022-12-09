package com.application.foggy.api.v1.orders.modals;

import lombok.Data;

@Data
public class MakePayment {
    private String id;
    private Double amount;
    private Double discount;
    private Double advance;
    private String paymentType;
}
