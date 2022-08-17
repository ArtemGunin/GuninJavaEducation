package com.repository;

import com.model.product.Toaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ToasterRepository implements CrudRepository<Toaster> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ToasterRepository.class);

    private final List<Toaster> toasters;

    private static ToasterRepository instance;

    private ToasterRepository() {
        toasters = new LinkedList<>();
    }

    public static ToasterRepository getInstance() {
        if (instance == null) {
            instance = new ToasterRepository();
        }
        return instance;
    }

    @Override
    public void save(Toaster toaster) {
        if (toaster == null) {
            final IllegalArgumentException exception = new IllegalArgumentException("Cannot save a null toaster");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        } else {
            toasters.add(toaster);
        }
    }

    @Override
    public void saveAll(List<Toaster> toasters) {
        toasters.forEach(this::save);
    }

    @Override
    public boolean update(Toaster toaster) {
        final Optional<Toaster> result = findById(toaster.getId());
        if (result.isEmpty()) {
            return false;
        }
        final Toaster originToaster = result.get();
        ToasterCopy.copy(toaster, originToaster);
        return true;
    }

    @Override
    public boolean delete(String id) {
        return toasters.removeIf(toaster -> {
            if (toaster.getId().equals(id)) {
                LOGGER.info("Remote toaster - {}", toaster);
                return true;
            }
            return false;
        });
    }

    @Override
    public List<Toaster> getAll() {
        return toasters;
    }

    @Override
    public Optional<Toaster> findById(String id) {
        return toasters.stream()
                .filter(toaster -> toaster.getId().equals(id))
                .findAny();
    }

    @Override
    public Optional<Toaster> getByIndex(int index) {
        return Optional.of(toasters.get(index));
    }

    private static class ToasterCopy {
        private static void copy(final Toaster from, final Toaster to) {
            to.setCount(from.getCount());
            to.setPrice(from.getPrice());
            to.setTitle(from.getTitle());
        }
    }

    @Override
    public boolean hasProduct(String id) {
        return toasters.stream()
                .anyMatch(toaster -> toaster.getId().equals(id));
    }
}
