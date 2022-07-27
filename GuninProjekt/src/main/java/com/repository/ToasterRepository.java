package com.repository;

import com.model.product.Toaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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
            checkDuplicates(toaster);
            toasters.add(toaster);
        }
    }

    private void checkDuplicates(Toaster toaster) {
        for (Toaster t : toasters) {
            if (toaster.hashCode() == t.hashCode() && toaster.equals(t)) {
                final IllegalArgumentException exception = new IllegalArgumentException("Duplicate tv: " +
                        toaster.getId());
                LOGGER.error(exception.getMessage(), exception);
                throw exception;
            }
        }
    }

    @Override
    public void saveAll(List<Toaster> toasters) {
        for (Toaster toaster : toasters) {
            save(toaster);
        }
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
        final Iterator<Toaster> iterator = toasters.iterator();
        while (iterator.hasNext()) {
            final Toaster toaster = iterator.next();
            if (toaster.getId().equals(id)) {
                LOGGER.info("Remote toaster - {}", toaster);
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Toaster> getAll() {
        if (toasters.isEmpty()) {
            return Collections.emptyList();
        }
        return toasters;
    }

    @Override
    public Optional<Toaster> findById(String id) {
        Toaster result = null;
        for (Toaster toaster : toasters) {
            if (toaster.getId().equals(id)) {
                result = toaster;
            }
        }
        return Optional.ofNullable(result);
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
        for (Toaster toaster : toasters) {
            if (toaster.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
