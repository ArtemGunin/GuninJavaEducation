package solid.com.example.repository;

import solid.com.example.model.ProductImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductRepositoryImpl implements ProductRepository<ProductImpl> {

    private static Map<Long, ProductImpl> repository;

    private static ProductRepositoryImpl instance;

    private ProductRepositoryImpl() {
        repository = new HashMap<>();
    }

    public static ProductRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new ProductRepositoryImpl();
        }
        return instance;
    }

    @Override
    public void save(ProductImpl product) {
        repository.put(product.getId(), product);
    }

    @Override
    public List<ProductImpl> getAll() {
        return new ArrayList<>(repository.values());
    }
}
