package com.service;

import com.model.product.Manufacturer;
import com.model.product.Phone;
import com.repository.CrudRepository;
import com.repository.PhoneRepository;

public class PhoneService extends ProductService<Phone> {

    private static PhoneService instance;

    private PhoneService(CrudRepository<Phone> repository) {
        super(repository);
    }

    public static PhoneService getInstance() {
        if (instance == null) {
            instance = new PhoneService(PhoneRepository.getInstance());
        }
        return instance;
    }

    public static PhoneService getInstance(final PhoneRepository repository) {
        if (instance == null) {
            instance = new PhoneService(repository);
        }
        return instance;
    }

    @Override
    protected Phone createProduct() {
        return new Phone(
                "Title-" + RANDOM.nextInt(1000),
                RANDOM.nextInt(500),
                RANDOM.nextDouble() * 1000,
                "Model-" + RANDOM.nextInt(10),
                getRandomManufacturer()
        );
    }

    @Override
    public Phone getProductWithModifiedId(String originalId, String newId) {
        Phone copiedPhone = repository.findById(originalId).orElseThrow(()
                -> new IllegalArgumentException("The base does not contain a product with this id - " + originalId));
        return new Phone(newId,
                copiedPhone.getTitle(),
                copiedPhone.getCount(),
                copiedPhone.getPrice(),
                copiedPhone.getModel(),
                copiedPhone.getManufacturer()
        );
    }

    @Override
    public Phone createDefaultProduct() {
        return new Phone("Custom", 0, 0.0, "Model", Manufacturer.SONY);
    }

    @Override
    public Phone createProductWithId(String id) {
        return new Phone(id, "Custom", 0, 0.0, "Model", Manufacturer.SONY);
    }
}
