package com.service;

import com.model.product.Body;
import com.model.product.Manufacturer;
import com.model.product.Toaster;
import com.repository.CrudRepository;
import com.repository.ToasterRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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
    public Toaster createProductFromMap(Map<String, Object> container) {
        Function<Map<String, Object>, Toaster> createToaster = fields -> new Toaster(
                fields.getOrDefault("title", "Default").toString(),
                ((Integer) fields.getOrDefault("count", 0)),
                ((Double) fields.getOrDefault("price", 0.0)),
                fields.getOrDefault("currency", "").toString(),
                fields.getOrDefault("model", "Model").toString(),
                ((Integer) fields.getOrDefault("power", 0)),
                ((Manufacturer) fields.getOrDefault("manufacturer", Manufacturer.APPLE)),
                (LocalDateTime) fields.getOrDefault("created", LocalDateTime.now()),
                (Body) fields.getOrDefault("body", new Body()));
        return createToaster.apply(container);
    }

    @Override
    protected Map<String, Object> convertStringsToObjectParameters(Map<String, String> parameters) {
        Map<String, Object> phoneContainer = new HashMap<>();
        Body body = new Body();
        body.setMaterial(parameters.get("material"));
        body.setColor(parameters.get("color"));
        phoneContainer.put("title", parameters.get("title"));
        phoneContainer.put("count", Integer.parseInt(parameters.get("count")));
        phoneContainer.put("price", Double.parseDouble(parameters.get("price")));
        phoneContainer.put("currency", parameters.get("currency"));
        phoneContainer.put("model", parameters.get("model"));
        phoneContainer.put("manufacturer", Manufacturer.valueOf(parameters.get("manufacturer")));
        phoneContainer.put("created", LocalDateTime.ofInstant(Instant.parse(parameters.get("created")), ZoneId.systemDefault()));
        phoneContainer.put("body", body);
        return phoneContainer;
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
