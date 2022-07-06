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
    private static final TVRepository REPOSITORY = new TVRepository();

    public void createAndSaveTVs(int count) {
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
        REPOSITORY.saveAll(tvs);
    }

    private TVManufacture getRandomManufacturer() {
        final TVManufacture[] values = TVManufacture.values();
        final int index = RANDOM.nextInt(values.length);
        return values[index];
    }

    public void printAll() {
        for (TV tv : REPOSITORY.getAll()) {
            System.out.println(tv);
        }
    }

    public TV useTVWithIndex(int index) {
        return REPOSITORY.getTVByIndex(index);
    }

    public void deleteTV(TV tv) {
        REPOSITORY.delete(tv.getId());
    }

    public void updateTV(TV tv) {
        if (REPOSITORY.update(tv)) {
            System.out.println("Updated TV " + REPOSITORY.findById(tv.getId()));
        } else {
            System.out.println("The TV is missing in the database");
        }
    }
}
