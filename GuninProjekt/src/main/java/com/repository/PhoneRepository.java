package com.repository;

import com.model.product.Phone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PhoneRepository implements CrudRepository<Phone> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneRepository.class);

    private final List<Phone> phones;

    private static PhoneRepository instance;

    private PhoneRepository() {
        phones = new LinkedList<>();
    }

    public static PhoneRepository getInstance() {
        if (instance == null) {
            instance = new PhoneRepository();
        }
        return instance;
    }

    @Override
    public void save(Phone phone) {
        if (phone == null) {
            final IllegalArgumentException exception = new IllegalArgumentException("Cannot save a null phone");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        } else {
            phones.add(phone);
        }
    }

    @Override
    public void saveAll(List<Phone> phones) {
        phones.forEach(this::save);
    }

    @Override
    public boolean update(Phone phone) {
        final Optional<Phone> result = getById(phone.getId());
        if (result.isEmpty()) {
            return false;
        }
        final Phone originPhone = result.get();
        PhoneCopy.copy(phone, originPhone);
        return true;
    }

    @Override
    public boolean delete(String id) {
        return phones.removeIf(phone -> {
            if (phone.getId().equals(id)) {
                LOGGER.info("Remote phone - {}", phone);
                return true;
            }
            return false;
        });
    }

    @Override
    public List<Phone> getAll() {
        return phones;
    }

    @Override
    public Optional<Phone> getById(String id) {
        return phones.stream()
                .filter(phone -> phone.getId().equals(id))
                .findAny();
    }

    @Override
    public Optional<Phone> getByIndex(int index) {
        return Optional.ofNullable(phones.get(index));
    }

    private static class PhoneCopy {
        private static void copy(final Phone from, final Phone to) {
            to.setCount(from.getCount());
            to.setPrice(from.getPrice());
            to.setTitle(from.getTitle());
        }
    }

    @Override
    public boolean hasProduct(String id) {
        return phones.stream()
                .anyMatch(phone -> phone.getId().equals(id));
    }
}
