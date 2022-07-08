package com.service;

import com.model.TV;
import com.model.TVManufacture;
import com.repository.TVRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TVService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TVService.class);

    private static final Random RANDOM = new Random();

    private final TVRepository repository;

    public TVService(TVRepository repository) {
        this.repository = repository;
    }

    public void createAndSaveTVs(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("count must been bigger then 0");
        }

        List<TV> tvs = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            tvs.add(new TV(
                    "Title-" + RANDOM.nextInt(1000),
                    RANDOM.nextInt(500),
                    RANDOM.nextDouble() * 1000,
                    "Model-" + RANDOM.nextInt(10),
                    getRandomManufacturer(),
                    14 + RANDOM.nextInt(52)
            ));
            LOGGER.info("new " + tvs.get(i).toString());
        }

        repository.saveAll(tvs);

    }

    private TVManufacture getRandomManufacturer() {
        final TVManufacture[] values = TVManufacture.values();
        final int index = RANDOM.nextInt(values.length);
        return values[index];
    }

    public void printAll() {

        for (TV tv : repository.getAll()) {

            System.out.println(tv);
        }
    }

    public List<TV> getAll() {
        return repository.getAll();
    }

    public TV useTVWithIndex(int index) {
        return repository.getTVByIndex(index);
    }

    public void deleteTV(TV tv) {
        repository.delete(tv.getId());
    }

    public void updateTV(TV tv) {
        if (!repository.update(tv)) {
            final IllegalArgumentException exception = new IllegalArgumentException(
                    "The TV is missing in the database");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }
        System.out.println("Updated TV " + repository.findById(tv.getId()));

    }
}
