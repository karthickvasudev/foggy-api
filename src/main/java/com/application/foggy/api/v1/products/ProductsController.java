package com.application.foggy.api.v1.products;

import com.application.foggy.api.v1.products.modals.CreateProductModal;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/products")
@AllArgsConstructor
public class ProductsController {
    private final ProductsService service;

    @PostMapping("create")
    public Product create(@RequestBody CreateProductModal product) {
        return service.createProduct(product);
    }

    @GetMapping()
    public List<Product> list() {
        return service.getProductList();
    }

    @GetMapping("{id}")
    public Product get(@PathVariable String id) {
        return service.getProduct(id);
    }

    @PutMapping("{id}")
    public Product update(@PathVariable String id, @RequestBody Product product) {
        return service.updateProduct(id, product);
    }
}
