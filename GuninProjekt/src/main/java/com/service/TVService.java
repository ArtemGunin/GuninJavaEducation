package com.service;

import com.model.product.Manufacturer;
import com.model.product.OperatingSystem;
import com.model.product.TV;
import com.repository.CrudRepository;
import com.repository.TVRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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
    public TV createProductFromMap(Map<String, Object> container) {
        Function<Map<String, Object>, TV> createTV = fields -> new TV(
                fields.getOrDefault("title", "Default").toString(),
                ((Integer) fields.getOrDefault("count", 0)),
                ((Double) fields.getOrDefault("price", 0.0)),
                fields.getOrDefault("currency", "").toString(),
                fields.getOrDefault("model", "Model").toString(),
                ((Manufacturer) fields.getOrDefault("manufacturer", Manufacturer.APPLE)),
                ((Integer) fields.getOrDefault("diagonal", 0)),
                (LocalDateTime) fields.getOrDefault("created", LocalDateTime.now()),
                (OperatingSystem) fields.getOrDefault("operatingSystem", new OperatingSystem()));
        return createTV.apply(container);
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
        phoneContainer.put("diagonal", Integer.parseInt(parameters.get("diagonal")));
        phoneContainer.put("created", LocalDateTime.parse(parameters.get("created")));
        phoneContainer.put("operatingSystem", operatingSystem);
        return phoneContainer;
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
