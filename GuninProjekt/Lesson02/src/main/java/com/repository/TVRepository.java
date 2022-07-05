package com.repository;

import com.model.Phone;
import com.model.TV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TVRepository implements CrudRepository<TV> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TVRepository.class);

    private final List<TV> tvs;

    public TVRepository() {
        tvs = new LinkedList<>();
    }

    @Override
    public void save(TV tv) {
        tvs.add(tv);
    }

    @Override
    public void saveAll(List<TV> tvs) {
        for (TV tv : tvs) {
            save(tv);
        }
    }

    @Override
    public boolean update(TV tv) {
        final Optional<TV> result = findById(tv.getId());
        if (result.isEmpty()) {
            return false;
        }
        final TV originTV = result.get();
        TVCopy.copy(tv, originTV);
        return true;
    }

    @Override
    public boolean delete(String id) {
        final Iterator<TV> iterator = tvs.iterator();
        while (iterator.hasNext()) {
            final TV tv = iterator.next();
            if (tv.getId().equals(id)) {
                LOGGER.info("Remote TV - " + tvs);
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public List<TV> getAll() {
        if (tvs.isEmpty()) {
            return Collections.emptyList();
        }
        return tvs;
    }

    @Override
    public Optional<TV> findById(String id) {
        TV result = null;
        for (TV tv : tvs) {
            if (tv.getId().equals(id)) {
                result = tv;
            }
        }
        return Optional.ofNullable(result);
    }

    private static class TVCopy {
        private static void copy(final TV from, final TV to) {
            to.setCount(from.getCount());
            to.setPrice(from.getPrice());
            to.setTitle(from.getTitle());
        }
    }

    public TV getTVByIndex(int index) {
        return tvs.get(index);
    }
}
