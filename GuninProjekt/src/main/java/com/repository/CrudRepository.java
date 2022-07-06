package com.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<E> {
    void save(E e);

    void saveAll(List<E> phones);

    boolean update(E e);

    boolean delete(String id);

    List<E> getAll();

    Optional<E> findById(String id);
}
