package com.application.foggy.api.v1.orders;

import com.application.foggy.api.v1.customers.Customer;
import com.application.foggy.api.v1.orders.enums.ORDERSTATUS;
import com.application.foggy.api.v1.transactions.PaymentTransaction;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
@Data
@Builder
@JsonInclude(JsonInclude.Include.ALWAYS)
@ToString
public class Order {
    @Id
    private String id;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String customerId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Customer customer;
    private Integer count;
    private Double amount;
    private ORDERSTATUS status;

    private String bill;
    @JsonFormat(pattern = "dd-MMM-yyyy hh:mm:ssa")
    private LocalDateTime orderDate;
    @JsonFormat(pattern = "dd-MMM-yyyy hh:mm:ssa")
    private LocalDateTime expectedDeliveryDate;
    @JsonFormat(pattern = "dd-MMM-yyyy hh:mm:ssa")
    private LocalDateTime completedDate;
    @JsonFormat(pattern = "dd-MMM-yyyy hh:mm:ssa")
    private LocalDateTime deliveredDate;
    private List<OrderLines> orderLines;
    private OrderPaymentDetails orderPaymentDetails;
    private List<PaymentTransaction> paymentTransaction;
    private String createdBy;
    @JsonFormat(pattern = "dd-MMM-yyyy hh:mm:ssa")
    private LocalDateTime createdOn;
    private String updatedBy;
    @JsonFormat(pattern = "dd-MMM-yyyy hh:mm:ssa")
    private LocalDateTime updatedOn;
}
