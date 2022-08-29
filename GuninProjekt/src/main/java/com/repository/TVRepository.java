package com.repository;

import com.context.Singleton;
import com.model.product.TV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Singleton
public class TVRepository implements CrudRepository<TV> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TVRepository.class);

    private final List<TV> tvs;

    private static TVRepository instance;

    private TVRepository() {
        tvs = new LinkedList<>();
    }

    public static TVRepository getInstance() {
        if (instance == null) {
            instance = new TVRepository();
        }
        return instance;
    }

    @Override
    public void save(TV tv) {
        if (tv == null) {
            final IllegalArgumentException exception = new IllegalArgumentException("Cannot save a null tv");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        } else {
            tvs.add(tv);
        }
    }

    @Override
    public void saveAll(List<TV> tvs) {
        tvs.forEach(this::save);
    }

    @Override
    public boolean update(TV tv) {
        final Optional<TV> result = getById(tv.getId());
        if (result.isEmpty()) {
            return false;
        }
        final TV originTV = result.get();
        TVCopy.copy(tv, originTV);
        return true;
    }

    @Override
    public boolean delete(String id) {
        return tvs.removeIf(tv -> {
            if (tv.getId().equals(id)) {
                LOGGER.info("Remote TV - {}", tv);
                return true;
            }
            return false;
        });
    }

    @Override
    public List<TV> getAll() {
        return tvs;
    }

    @Override
    public Optional<TV> getById(String id) {
        return tvs.stream()
                .filter(tv -> tv.getId().equals(id))
                .findAny();
    }

    @Override
    public Optional<TV> getByIndex(int index) {
        return Optional.ofNullable(tvs.get(index));
    }

    private static class TVCopy {
        private static void copy(final TV from, final TV to) {
            to.setCount(from.getCount());
            to.setPrice(from.getPrice());
            to.setTitle(from.getTitle());
        }
    }

    @Override
    public boolean hasProduct(String id) {
        return tvs.stream()
                .anyMatch(tv -> tv.getId().equals(id));
    }
}
