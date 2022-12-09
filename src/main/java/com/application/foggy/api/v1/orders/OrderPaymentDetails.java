package com.application.foggy.api.v1.orders;

import com.application.foggy.api.v1.orders.enums.PAYMENTSTATUS;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class OrderPaymentDetails {
    private Double advance;
    private Double balance;
    private Double discount;
    private Double paidAmount;
    private PAYMENTSTATUS status;
}
