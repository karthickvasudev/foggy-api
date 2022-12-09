package com.application.foggy.api.v1.products.modals;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductModal {
    private String name;
    private Integer quantity;
    private Double price;
    private Boolean active;
}
