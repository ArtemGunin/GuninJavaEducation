package com.service;

import com.context.Autowired;
import com.context.Singleton;
import com.model.product.Body;
import com.model.product.Manufacturer;
import com.model.product.Toaster;
import com.repository.ToasterRepositoryDB;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Singleton
public class ToasterService extends ProductService<Toaster> {

    private final ToasterRepositoryDB repository;

    private static ToasterService instance;

    @Autowired
    private ToasterService(final ToasterRepositoryDB repository) {
        super(repository);
        this.repository = repository;
    }

    public static ToasterService getInstance() {
        if (instance == null) {
            instance = new ToasterService(ToasterRepositoryDB.getInstance());
        }
        return instance;
    }

    public static ToasterService getInstance(final ToasterRepositoryDB repository) {
        if (instance == null) {
            instance = new ToasterService(repository);
        }
        return instance;
    }

    @Override
    protected Toaster createProduct() {
        return new Toaster.ToasterBuilder()
                .setTitle("Title-" + RANDOM.nextInt(1000))
                .setCount(RANDOM.nextInt(500))
                .setPrice(RANDOM.nextDouble() * 1000)
                .setModel("Model-" + RANDOM.nextInt(10))
                .setPower(1000 + RANDOM.nextInt(2000))
                .setManufacturer(Manufacturer.PHILIPS)
                .build();
    }

    @Override
    protected Toaster getProductWithModifiedId(String originalId, String newId) {
        Toaster copiedToaster = repository.getById(originalId).orElseThrow(()
                -> new IllegalArgumentException("The base does not contain a product with this id - " + originalId));
        return new Toaster.ToasterBuilder()
                .setId(newId)
                .setTitle(copiedToaster.getTitle())
                .setCount(copiedToaster.getCount())
                .setPrice(copiedToaster.getPrice())
                .setModel(copiedToaster.getModel())
                .setPower(copiedToaster.getPower())
                .setManufacturer(copiedToaster.getManufacturer())
                .build();
    }

    public Toaster createProductFromMap(Map<String, Object> container) {
        Function<Map<String, Object>, Toaster> createToaster = fields -> new Toaster.ToasterBuilder()
                .setTitle(fields.getOrDefault("title", "Default").toString())
                .setCount((Integer) fields.getOrDefault("count", 0))
                .setPrice((Double) fields.getOrDefault("price", 0.0))
                .setCurrency(fields.getOrDefault("currency", "").toString())
                .setModel(fields.getOrDefault("model", "Model").toString())
                .setPower((Integer) fields.getOrDefault("power", 0))
                .setManufacturer((Manufacturer) fields.getOrDefault("manufacturer", Manufacturer.APPLE))
                .setLocalDateTime((LocalDateTime) fields.getOrDefault("created", LocalDateTime.now()))
                .setBody((Body) fields.getOrDefault("body", new Body("", "")))
                .build();

        return createToaster.apply(container);
    }

    @Override
    protected Map<String, Object> convertStringsToObjectParameters(Map<String, String> parameters) {
        Map<String, Object> phoneContainer = new HashMap<>();
        Body body = new Body(parameters.get("material"), parameters.get("color"));
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
        return new Toaster.ToasterBuilder()
                .setTitle("Custom")
                .setCount(0)
                .setPrice(0.0)
                .setModel("Model")
                .setManufacturer(Manufacturer.SONY)
                .build();
    }

    @Override
    protected Toaster createProductWithId(String id) {
        return new Toaster.ToasterBuilder()
                .setId(id)
                .setTitle("Custom")
                .setCount(0)
                .setPrice(0.0)
                .setModel("Model")
                .setManufacturer(Manufacturer.SONY)
                .build();
    }
}
