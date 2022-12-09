package com.application.foggy.documentnumbering;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "document_numbering")
@Builder
public class DocumentNumbering {
    @Id
    private String id;
    private String product;
    private String customer;
    private String order;
    private String transaction;
}
