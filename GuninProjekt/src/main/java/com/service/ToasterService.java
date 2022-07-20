package com.service;

import com.model.Manufacturer;
import com.model.Toaster;
import com.repository.CrudRepository;

public class ToasterService extends ProductService<Toaster> {

    public ToasterService(CrudRepository<Toaster> repository) {
        super(repository);
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
