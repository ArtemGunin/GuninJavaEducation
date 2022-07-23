package com.service;

import com.model.product.Manufacturer;
import com.model.product.Product;
import com.repository.CrudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public abstract class ProductService<T extends Product> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneService.class);
    protected static final Random RANDOM = new Random();
    protected final CrudRepository<T> repository;

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
}
