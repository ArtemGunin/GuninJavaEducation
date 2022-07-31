package com.service;

import com.model.product.Manufacturer;
import com.model.product.Toaster;
import com.repository.CrudRepository;
import com.repository.ToasterRepository;

public class ToasterService extends ProductService<Toaster> {

    private static ToasterService instance;

    private ToasterService(CrudRepository<Toaster> repository) {
        super(repository);
    }

    public static ToasterService getInstance() {
        if (instance == null) {
            instance = new ToasterService(ToasterRepository.getInstance());
        }
        return instance;
    }

    public static ToasterService getInstance(final ToasterRepository repository) {
        if (instance == null) {
            instance = new ToasterService(repository);
        }
        return instance;
    }

    @Override
    protected Toaster createProduct() {
        return new Toaster(
                "Title-" + RANDOM.nextInt(1000),
                RANDOM.nextInt(500),
                RANDOM.nextDouble() * 1000,
                "Model-" + RANDOM.nextInt(10),
                1000 + RANDOM.nextInt(2000),
                getRandomManufacturer()
        );
    }

    @Override
    protected Toaster getProductWithModifiedId(String originalId, String newId) {
        Toaster copiedToaster = repository.findById(originalId).orElseThrow(()
                -> new IllegalArgumentException("The base does not contain a product with this id - " + originalId));
        return new Toaster(newId,
                copiedToaster.getTitle(),
                copiedToaster.getCount(),
                copiedToaster.getPrice(),
                copiedToaster.getModel(),
                copiedToaster.getPower(),
                copiedToaster.getManufacturer());
    }

    @Override
    protected Toaster createDefaultProduct() {
        return new Toaster("Custom", 0, 0.0, "Model", 0, Manufacturer.SONY);
    }

    @Override
    protected Toaster createProductWithId(String id) {
        return new Toaster(id, "Custom", 0, 0.0, "Model", 0, Manufacturer.SONY);
    }
}
