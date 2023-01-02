package com.application.foggy.api.v1.customers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "customers")
public class Customer {
    @Id
    private String id;
    private String name;
    @Indexed
    private String phoneNumber;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double pendingAmount;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double advanceAmount;
    private Double amount;
    private Address address;
    @JsonFormat(pattern = "dd-MMM-yyyy hh:mm:ssa")
    private LocalDateTime createdOn;
    private String createdBy;
    @JsonFormat(pattern = "dd-MMM-yyyy hh:mm:ssa")
    private LocalDateTime updatedOn;
    private String updatedBy;
}
