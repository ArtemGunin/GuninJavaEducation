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
    private static final PhoneRepository REPOSITORY = new PhoneRepository();

    public void createAndSavePhones(int count) {
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
        REPOSITORY.saveAll(phones);
    }

    private PhoneManufacture getRandomManufacturer() {
        final PhoneManufacture[] values = PhoneManufacture.values();
        final int index = RANDOM.nextInt(values.length);
        return values[index];
    }

    public void printAll() {
        for (Phone phone : REPOSITORY.getAll()) {
            System.out.println(phone);
        }
    }

    public Phone usePhoneWithIndex(int index) {
        return REPOSITORY.getPhoneByIndex(index);
    }

    public void deletePhone(Phone phone) {
        REPOSITORY.delete(phone.getId());
    }

    public void updatePhone(Phone phone) {
        if (REPOSITORY.update(phone)) {
            System.out.println("Updated phone " + REPOSITORY.findById(phone.getId()));
        } else {
            System.out.println("The phone is missing in the database");
        }
    }
}
