package com.repository;

import com.model.product.Product;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T extends Product> {
    void save(T t);

    void saveAll(List<T> products);

    boolean update(T t);

    boolean delete(String id);

    List<T> getAll();

    Optional<T> findById(String id);

    Optional<T> getByIndex(int index);

    boolean hasProduct(String id);
}
