package com.example.service;

import com.example.model.ProductImpl;
import com.example.repository.ProductRepositoryImpl;

import java.util.List;

public class ProductService {
    protected final ProductRepositoryImpl repository;

    private static ProductService instance;

    protected ProductService(ProductRepositoryImpl repository) {
        this.repository = repository;
    }

    public static ProductService getInstance() {
        if (instance == null) {
            instance = new ProductService(ProductRepositoryImpl.getInstance());
        }
        return instance;
    }

    public void saveProduct(ProductImpl product) {
        repository.save(product);
    }

    public List<ProductImpl> getAll() {
        return repository.getAll();
    }
}
