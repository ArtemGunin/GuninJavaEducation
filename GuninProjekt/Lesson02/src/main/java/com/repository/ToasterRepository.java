package com.repository;

import com.model.Toaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ToasterRepository implements CrudRepository<Toaster> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ToasterRepository.class);

    private final List<Toaster> toasters;

    public ToasterRepository() {
        toasters = new LinkedList<>();
    }

    @Override
    public void save(Toaster toaster) {
        toasters.add(toaster);
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
                LOGGER.info("Remote toaster - " + toaster);
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

    private static class ToasterCopy {
        private static void copy(final Toaster from, final Toaster to) {
            to.setCount(from.getCount());
            to.setPrice(from.getPrice());
            to.setTitle(from.getTitle());
        }
    }

    public Toaster getToasterByIndex(int index) {
        return toasters.get(index);
    }

}
