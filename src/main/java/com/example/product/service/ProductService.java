package com.example.product.service;

import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

//    public List<Product> getAllProducts() {
//        return productRepository.findAll();
//    }
    public Page<Product> getAllProducts(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            return (Page<Product>) productRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    public Optional<Product> getProductById(@PathVariable Long id){
        return productRepository.findById(id);
    }

    @Transactional
    public Product createProduct(Product product){
        Optional<Product> existingProduct = productRepository.findByName(product.getName());

        if (existingProduct.isPresent()) {
            throw new IllegalArgumentException("Product with name '" + product.getName() + "' already exists.");
        }

        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id).map(product -> {
            product.setName(updatedProduct.getName());
            product.setPrice(updatedProduct.getPrice());
            product.setQuantity(updatedProduct.getQuantity());
            product.setDescription(updatedProduct.getDescription());
            return productRepository.save(product);
        }).orElse(null);
    }

    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }
    public Page<Product> searchProducts(String name, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(name, pageable);
    }

}
