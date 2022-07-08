package com.service;

import com.model.Toaster;
import com.model.ToasterManufacture;
import com.repository.ToasterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ToasterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ToasterService.class);

    private static final Random RANDOM = new Random();

    private final ToasterRepository repository;

    public ToasterService(ToasterRepository repository) {
        this.repository = repository;
    }

    public void createAndSaveToasters(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("count must been bigger then 0");
        }

        List<Toaster> toasters = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            toasters.add(new Toaster(
                    "Title-" + RANDOM.nextInt(1000),
                    RANDOM.nextInt(500),
                    RANDOM.nextDouble() * 1000,
                    "Model-" + RANDOM.nextInt(10),
                    1000 + RANDOM.nextInt(2000),
                    getRandomManufacturer()
            ));
            LOGGER.info("new " + toasters.get(i).toString());
        }

        repository.saveAll(toasters);

    }

    private ToasterManufacture getRandomManufacturer() {
        final ToasterManufacture[] values = ToasterManufacture.values();
        final int index = RANDOM.nextInt(values.length);
        return values[index];
    }

    public void printAll() {

        for (Toaster toaster : repository.getAll()) {

            System.out.println(toaster);
        }
    }

    public List<Toaster> getAll() {
        return repository.getAll();
    }

    public Toaster useToasterWithIndex(int index) {
        return repository.getToasterByIndex(index);
    }

    public void deleteToaster(Toaster toaster) {
        repository.delete(toaster.getId());
    }

    public void updateToaster(Toaster toaster) {
        if (!repository.update(toaster)) {
            final IllegalArgumentException exception = new IllegalArgumentException(
                    "The toaster is missing in the database");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }
        System.out.println("Updated toaster " + repository.findById(toaster.getId()));

    }
}

