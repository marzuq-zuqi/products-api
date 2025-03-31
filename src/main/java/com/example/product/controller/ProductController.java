package com.example.product.controller;

import com.example.product.model.Product;
import com.example.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

//    @GetMapping
//    public ResponseEntity<?> getProducts(
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "10") int limit,
//            @RequestParam(defaultValue = "") String search) {
//
//        Pageable pageable = PageRequest.of(page - 1, limit);
//        Page<Product> productPage;
//
//        if (!search.isEmpty()) {
//            productPage = productRepository.findByNameContainingIgnoreCase(search, pageable);
//        } else {
//            productPage = productRepository.findAll(pageable);
//        }
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("products", productPage.getContent());
//        response.put("totalPages", productPage.getTotalPages());
//
//        return ResponseEntity.ok(response);
//    }


    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        try {
            return ResponseEntity.ok(productService.createProduct(product));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody Product updatedProduct, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        Product updated = productService.updateProduct(id, updatedProduct);
        if (updated == null) {
            return ResponseEntity.badRequest().body("Product not found.");
        }

        return ResponseEntity.ok(updated);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
        return ResponseEntity.ok(productService.searchProducts(name));
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return "Product deleted successfully";
    }

}
