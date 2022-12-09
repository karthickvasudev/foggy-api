package com.application.foggy.api.v1.products;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "products")
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Product {
    @Id
    private String id;
    private String name;
    private Integer quantity;
    private Double price;
    private Boolean active;
    private String createdBy;
    @JsonFormat(pattern = "dd-MMM-yyyy hh:mm:ssa")
    private LocalDateTime createdOn;
    private String updatedBy;
    @JsonFormat(pattern = "dd-MMM-yyyy hh:mm:ssa")
    private LocalDateTime updatedOn;
}
