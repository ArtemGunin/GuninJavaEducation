package com.service;

import com.model.product.Manufacturer;
import com.model.product.OperatingSystem;
import com.model.product.Phone;
import com.repository.CrudRepository;
import com.repository.PhoneRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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
    public Phone createProductFromMap(Map<String, Object> container) {
        Function<Map<String, Object>, Phone> createPhone = fields -> new Phone(
                fields.getOrDefault("title", "Default").toString(),
                ((Integer) fields.getOrDefault("count", 0)),
                ((Double) fields.getOrDefault("price", 0.0)),
                fields.getOrDefault("currency", "").toString(),
                fields.getOrDefault("model", "Model").toString(),
                ((Manufacturer) fields.getOrDefault("manufacturer", Manufacturer.APPLE)),
                (LocalDateTime) fields.getOrDefault("created", LocalDateTime.now()),
                (OperatingSystem) fields.getOrDefault("operatingSystem", new OperatingSystem()));
        return createPhone.apply(container);
    }

    @Override
    protected Map<String, Object> convertStringsToObjectParameters(Map<String, String> parameters) {
        Map<String, Object> phoneContainer = new HashMap<>();
        OperatingSystem operatingSystem = new OperatingSystem();
        operatingSystem.setDesignation(parameters.get("designation"));
        operatingSystem.setVersion(Integer.parseInt(parameters.get("version")));
        phoneContainer.put("title", parameters.get("title"));
        phoneContainer.put("count", Integer.parseInt(parameters.get("count")));
        phoneContainer.put("price", Double.parseDouble(parameters.get("price")));
        phoneContainer.put("currency", parameters.get("currency"));
        phoneContainer.put("model", parameters.get("model"));
        phoneContainer.put("manufacturer", Manufacturer.valueOf(parameters.get("manufacturer")));
        phoneContainer.put("created", LocalDateTime.parse(parameters.get("created")));
        phoneContainer.put("operatingSystem", operatingSystem);
        return phoneContainer;
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
