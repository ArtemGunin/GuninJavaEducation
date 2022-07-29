package com.service;

import com.model.product.*;
import com.repository.CrudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class ProductService<T extends Product> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneService.class);
    protected static final Random RANDOM = new Random();
    protected final CrudRepository<T> repository;

    private static final Predicate<Product> CHECK_IS_PRESENT_PRISE = product -> product.getPrice() > 0.0;

    protected ProductService(CrudRepository<T> repository) {
        this.repository = repository;
    }

    public void createAndSaveProducts(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("count must been bigger then 0");
        }
        List<T> products = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            final T product = createProduct();
            products.add(product);
            LOGGER.info("new " + products.get(i).toString());
        }
        repository.saveAll(products);
    }

    protected abstract T createProduct();

    protected abstract T createProductWithId(String id);

    protected abstract T createDefaultProduct();

    protected abstract T getProductWithModifiedId(String originalId, String newId);


    public void saveProduct(T t) {
        if (t.getCount() == 0) {
            t.setCount(-1);
        }
        repository.save(t);
    }

    protected Manufacturer getRandomManufacturer() {
        final Manufacturer[] values = Manufacturer.values();
        final int index = RANDOM.nextInt(values.length);
        return values[index];
    }

    public List<T> getAll() {
        return repository.getAll();
    }

    public void printAll() {
        for (T t : repository.getAll()) {
            System.out.println(t);
        }
    }

    public T useProductWithIndex(int index) {
        return repository.getByIndex(index).orElseThrow();
    }

    public void deleteProduct(T t) {
        repository.delete(t.getId());
    }

    public void updateProduct(T t) {
        if (!repository.update(t)) {
            final IllegalArgumentException exception = new IllegalArgumentException(
                    "The product is missing in the database");
            LOGGER.error(exception.getMessage(), exception);
            throw exception;
        }
        System.out.println("Updated product " + repository.findById(t.getId()));
    }

    public void resetProductIfPresent(String id) {
        repository.findById(id).ifPresent(originalProduct -> {
            originalProduct.setCount(0);
            originalProduct.setPrice(0.0);
            originalProduct.setTitle("Custom");
        });
    }

    public boolean productIsPresent(String id) {
        return repository.findById(id).isPresent();
    }

    public T getProductOrCreate(String id) {
        return repository.findById(id).orElse(createProductWithId(id));
    }

    public void saveProductOrCreate(T t) {
        final Optional<T> productOptional = Optional.ofNullable(t);
        repository.save(productOptional.orElseGet(this::createDefaultProduct));
    }

    public void updateProductOrCreateDefault(T t) {
        final Optional<T> productOptional = Optional.ofNullable(t);
        productOptional.ifPresentOrElse(
                updatedProduct -> {
                    if (repository.hasProduct(updatedProduct.getId())) {
                        repository.update(updatedProduct);
                    } else {
                        repository.save(createDefaultProduct());
                    }
                },
                ()
                        -> repository.save(createDefaultProduct())
        );
    }

    public Double getCoastOfProducts(String id) {
        return repository.findById(id)
                .map(product -> product.getPrice() * product.getCount())
                .orElseThrow(() -> new IllegalArgumentException
                        ("The base does not contain a phone with this id - " + id));
    }

    public Boolean productPriceMiddleBounds(String id, double lowPrice, double highPrice) {
        return repository.findById(id).filter(product -> product.getPrice() >= lowPrice)
                .filter(product -> product.getPrice() < highPrice)
                .isPresent();
    }

    public T getProductWithIdOrCreatedIfProductMiss(String id) {
        return repository.findById(id).or(() -> Optional.of(createProductWithId(id)))
                .orElseThrow(() -> new NullPointerException("Can not return the product"));
    }

    public void printProductsExpensiveThen(double price) {
        System.out.println("Prises more then - " + price + " :");
        repository.getAll().stream()
                .filter(product -> product.getPrice() > price)
                .forEach(product -> System.out.println(product.getTitle()));
    }

    public int SumCounts() {
        return repository.getAll().stream()
                .reduce(0, (countSum, product) -> countSum + product.getCount(), Integer::sum);
    }

    public Map<String, ProductType> sortByTitleClearDuplicatesConvertToMap() {
        return repository.getAll().stream()
                .sorted(Comparator.comparing(Product::getTitle))
                .distinct()
                .collect(Collectors.toMap(Product::getId, Product::getType, (o1, o2) -> o2));
    }

    public boolean detailIsPresent(String checkedDetail) {
        return repository.getAll().stream()
                .filter(product -> product.getDetails() != null)
                .flatMap(product -> product.getDetails().stream())
                .anyMatch(checkedDetail::equals);
    }

    public DoubleSummaryStatistics getPriceSummaryStatistic() {
        return repository.getAll().stream()
                .mapToDouble(Product::getPrice)
                .summaryStatistics();
    }

    public boolean allProductsHasPrice() {
        return repository.getAll().stream()
                .allMatch(CHECK_IS_PRESENT_PRISE);
    }

    public Product createProductFromMap(Map<String, Object> productContainer) {
        Function<Map<String, Object>, Product> createProduct = fields ->
                switch ((ProductType) fields.get("productType")) {
                    case PHONE -> new Phone(
                            fields.getOrDefault("title", "Default").toString(),
                            ((Integer) fields.getOrDefault("count", 0)),
                            ((Double) fields.getOrDefault("price", 0.0)),
                            fields.getOrDefault("model", "Model").toString(),
                            ((Manufacturer) fields.getOrDefault("manufacturer", Manufacturer.APPLE)));
                    case TOASTER -> new Toaster(
                            fields.getOrDefault("title", "Default").toString(),
                            ((Integer) fields.getOrDefault("count", 0)),
                            ((Double) fields.getOrDefault("price", 0.0)),
                            fields.getOrDefault("model", "Model").toString(),
                            ((Integer) fields.getOrDefault("power", 0)),
                            ((Manufacturer) fields.getOrDefault("manufacturer", Manufacturer.APPLE)));
                    case TV -> new TV(
                            fields.getOrDefault("title", "Default").toString(),
                            ((Integer) fields.getOrDefault("count", 0)),
                            ((Double) fields.getOrDefault("price", 0.0)),
                            fields.getOrDefault("model", "Model").toString(),
                            ((Manufacturer) fields.getOrDefault("manufacturer", Manufacturer.APPLE)),
                            ((Integer) fields.getOrDefault("power", 0)));
                };
        return createProduct.apply(productContainer);
    }
}
