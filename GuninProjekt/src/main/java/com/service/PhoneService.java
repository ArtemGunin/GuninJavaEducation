package com.service;

import com.model.Phone;
import com.model.PhoneManufacture;
import com.repository.PhoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class PhoneService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneService.class);

    private static final Random RANDOM = new Random();
    private final PhoneRepository repository;

    public PhoneService(PhoneRepository repository) {
        this.repository = repository;
    }

    public void createAndSavePhones(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("count must been bigger then 0");
        }
        List<Phone> phones = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            phones.add(new Phone(
                    "Title-" + RANDOM.nextInt(1000),
                    RANDOM.nextInt(500),
                    RANDOM.nextDouble() * 1000,
                    "Model-" + RANDOM.nextInt(10),
                    getRandomManufacturer()
            ));
            LOGGER.info("new " + phones.get(i).toString());
        }
        repository.saveAll(phones);
    }

    public void savePhone(Phone phone) {
        if (phone.getCount() == 0) {
            phone.setCount(-1);
        }
        repository.save(phone);
    }

    private PhoneManufacture getRandomManufacturer() {
        final PhoneManufacture[] values = PhoneManufacture.values();
        final int index = RANDOM.nextInt(values.length);
        return values[index];
    }

    public void printAll() {
        for (Phone phone : repository.getAll()) {
            System.out.println(phone);
        }
    }

    public List<Phone> getAll() {
        return repository.getAll();
    }

    public Phone usePhoneWithIndex(int index) {
        return repository.getPhoneByIndex(index);
    }

    public void deletePhone(Phone phone) {
        repository.delete(phone.getId());
    }

    public void updatePhone(Phone phone) {
        if (!repository.update(phone)) {
            final IllegalArgumentException exception = new IllegalArgumentException(
                    "The phone is missing in the database");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }
        System.out.println("Updated phone " + repository.findById(phone.getId()));
    }
}
