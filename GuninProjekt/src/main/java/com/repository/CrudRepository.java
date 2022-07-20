package com.repository;

import com.model.Product;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T extends Product> {
    void save(T t);

    void saveAll(List<T> phones);

    boolean update(T t);

    boolean delete(String id);

    List<T> getAll();

    Optional<T> findById(String id);

    Optional<T> getByIndex(int index);

    boolean hasProduct (String id);
}
