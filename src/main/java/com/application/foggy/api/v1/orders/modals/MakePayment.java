package com.application.foggy.api.v1.orders.modals;

import com.application.foggy.api.v1.transactions.enums.PAYMENTTYPE;
import lombok.Data;

@Data
public class MakePayment {
    private String id;
    private Double amount;
    private Double discount = 0.0;
    private Double advance = 0.0;
    private PAYMENTTYPE paymentType;
}
