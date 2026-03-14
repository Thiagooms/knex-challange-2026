package com.knex.backend.controllers;

import com.knex.backend.dtos.product.ProductCreateDto;
import com.knex.backend.dtos.product.ProductResponseDto;
import com.knex.backend.dtos.product.ProductUpdateDto;
import com.knex.backend.services.ProductService;
import com.knex.backend.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final SecurityUtils securityUtils;

    public ProductController(ProductService productService, SecurityUtils securityUtils) {
        this.productService = productService;
        this.securityUtils = securityUtils;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> listAll() {
        List<ProductResponseDto> products = productService.listAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getById(@PathVariable Long id) {
        ProductResponseDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductCreateDto createDto) {
        String userEmail = securityUtils.getAuthenticatedUserEmail();
        ProductResponseDto product = productService.createProduct(createDto, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateDto updateDto
    ) {
        String userEmail = securityUtils.getAuthenticatedUserEmail();
        ProductResponseDto product = productService.updateProduct(id, updateDto, userEmail);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        String userEmail = securityUtils.getAuthenticatedUserEmail();
        productService.deleteProduct(id, userEmail);
        return ResponseEntity.noContent().build();
    }
}
