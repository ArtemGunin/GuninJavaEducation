package com.example.service;

import com.example.model.ProductBundle;
import com.example.model.ProductType;
import com.example.repository.ProductRepositoryImpl;

import java.util.Random;

public class ProductBundleService extends ProductService {

    protected ProductBundleService(ProductRepositoryImpl repository) {
        super(repository);
    }

    public ProductBundle createRandomProduct() {
        Random random = new Random();
        ProductBundle productBundle = new ProductBundle();
        productBundle.setAmount(random.nextInt(15));
        productBundle.setAvailable(random.nextBoolean());
        productBundle.setType(ProductType.PRODUCT_BUNDLE);
        productBundle.setPrice(random.nextDouble());
        productBundle.setId(random.nextLong());
        productBundle.setTitle(random.nextFloat() + "" + random.nextDouble());
        return productBundle;
    }
}
