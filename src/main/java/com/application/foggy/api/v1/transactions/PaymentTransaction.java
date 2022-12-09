package com.application.foggy.api.v1.transactions;

import com.application.foggy.api.v1.transactions.enums.PAYMENTTYPE;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "payment_transaction")
@Builder
public class PaymentTransaction {
    @Id
    private String id;
    private String orderId;
    private Double paidAmount;
    private PAYMENTTYPE paymentType;
    private String reference;
    private String createdBy;
    private LocalDateTime createdOn;
}
