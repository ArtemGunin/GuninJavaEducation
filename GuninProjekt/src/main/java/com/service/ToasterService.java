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
    private static final ToasterRepository REPOSITORY = new ToasterRepository();

    public void createAndSaveToasters(int count) {
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
        REPOSITORY.saveAll(toasters);
    }

    private ToasterManufacture getRandomManufacturer() {
        final ToasterManufacture[] values = ToasterManufacture.values();
        final int index = RANDOM.nextInt(values.length);
        return values[index];
    }

    public void printAll() {
        for (Toaster toaster : REPOSITORY.getAll()) {
            System.out.println(toaster);
        }
    }

    public Toaster useToasterWithIndex(int index) {
        return REPOSITORY.getToasterByIndex(index);
    }

    public void deleteToaster(Toaster toaster) {
        REPOSITORY.delete(toaster.getId());
    }

    public void updateToaster(Toaster toaster) {
        if (REPOSITORY.update(toaster)) {
            System.out.println("Updated toaster " + REPOSITORY.findById(toaster.getId()));
        } else {
            System.out.println("The toaster is missing in the database");
        }
    }
}

