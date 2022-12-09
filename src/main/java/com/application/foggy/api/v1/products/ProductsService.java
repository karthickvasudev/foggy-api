package com.application.foggy.api.v1.products;

import com.application.foggy.api.v1.products.modals.CreateProductModal;
import com.application.foggy.documentnumbering.DocumentNumberingService;
import com.application.foggy.support.exceptionresponse.ErrorResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductsService {
    private ProductsRepository repository;
    private DocumentNumberingService documentNumberingService;

    public Product createProduct(CreateProductModal product) {
        Product pdt = Product.builder()
                .id(documentNumberingService.getDocumentNumber(DocumentNumberingService.PRODUCT))
                .name(product.getName())
                .active(product.getActive())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .createdBy("username")
                .createdOn(LocalDateTime.now())
                .build();
        Product inserted = repository.insert(pdt);
        documentNumberingService.increment(DocumentNumberingService.PRODUCT);
        return inserted;
    }

    public List<Product> getProductList() {
        return repository.findAll();
    }

    public Product getProduct(String id) {
        try {
            return repository.findById(id).get();
        } catch (Exception e) {
            throw new ErrorResponse(HttpStatus.NOT_FOUND, String.format("%s - product not found", id));
        }

    }

    public Product updateProduct(String id, Product product) {
        Product p = getProduct(id);
        p.setName(product.getName());
        p.setQuantity(product.getQuantity());
        p.setActive(product.getActive());
        p.setPrice(product.getPrice());
        p.setUpdatedBy("username");
        p.setUpdatedOn(LocalDateTime.now());
        return repository.save(p);
    }
}
