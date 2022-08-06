package solid.com.example.service;

import solid.com.example.model.ProductBundle;
import solid.com.example.repository.ProductRepositoryImpl;

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
        productBundle.setPrice(random.nextDouble());
        productBundle.setId(random.nextLong());
        productBundle.setTitle(random.nextFloat() + "" + random.nextDouble());
        return productBundle;
    }
}
