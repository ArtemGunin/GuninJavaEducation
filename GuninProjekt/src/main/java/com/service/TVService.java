package com.service;

import com.model.product.Manufacturer;
import com.model.product.TV;
import com.repository.CrudRepository;
import com.repository.TVRepository;

public class TVService extends ProductService<TV> {

    private static TVService instance;

    private TVService(CrudRepository<TV> repository) {
        super(repository);
    }

    public static TVService getInstance() {
        if (instance == null) {
            instance = new TVService(TVRepository.getInstance());
        }
        return instance;
    }

    public static TVService getInstance(final TVRepository repository) {
        if (instance == null) {
            instance = new TVService(repository);
        }
        return instance;
    }

    @Override
    protected TV createProduct() {
        return new TV(
                "Title-" + RANDOM.nextInt(1000),
                RANDOM.nextInt(500),
                RANDOM.nextDouble() * 1000,
                "Model-" + RANDOM.nextInt(10),
                getRandomManufacturer(),
                14 + RANDOM.nextInt(52)
        );
    }

    @Override
    protected TV getProductWithModifiedId(String originalId, String newId) {
        TV copiedTV = repository.findById(originalId).orElseThrow(()
                -> new IllegalArgumentException("The base does not contain a product with this id - " + originalId));
        return new TV(newId,
                copiedTV.getTitle(),
                copiedTV.getCount(),
                copiedTV.getPrice(),
                copiedTV.getModel(),
                copiedTV.getManufacturer(),
                copiedTV.getDiagonal()
        );
    }

    @Override
    protected TV createDefaultProduct() {
        return new TV("Custom", 0, 0.0, "Model", Manufacturer.SONY, 0);
    }

    @Override
    protected TV createProductWithId(String id) {
        return new TV(id, "Custom", 0, 0.0, "Model", Manufacturer.SONY, 0);
    }
}
