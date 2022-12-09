package com.application.foggy.api.v1.orders;

import com.application.foggy.api.v1.products.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Data
public class OrderLines {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String productId;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Product product;
    private Integer count;
    private Double price;
}
