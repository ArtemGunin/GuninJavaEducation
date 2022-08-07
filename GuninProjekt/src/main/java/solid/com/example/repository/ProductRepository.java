package solid.com.example.repository;

import solid.com.example.model.ProductImpl;

import java.util.List;

public interface ProductRepository<T extends ProductImpl> {
    void save(T t);

    List<T> getAll();
}
