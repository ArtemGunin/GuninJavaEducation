package com.service;

import com.model.product.Manufacturer;
import com.model.product.Product;
import com.model.product.ProductType;
import com.repository.CrudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class ProductService<T extends Product> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);
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

    protected abstract T createProductFromMap(Map<String, Object> container);

    protected abstract Map<String, Object> convertStringsToObjectParameters(Map<String, String> parameters);

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
        repository.getAll().forEach(System.out::println);
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
        System.out.println("Updated product " + repository.getById(t.getId()));
    }

    public void resetProductIfPresent(String id) {
        repository.getById(id).ifPresent(originalProduct -> {
            originalProduct.setCount(0);
            originalProduct.setPrice(0.0);
            originalProduct.setTitle("Custom");
        });
    }

    public boolean productIsPresent(String id) {
        return repository.getById(id).isPresent();
    }

    public T getProductOrCreate(String id) {
        return repository.getById(id).orElse(createProductWithId(id));
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
                () -> repository.save(createDefaultProduct())
        );
    }

    public Double getCoastOfProducts(String id) {
        return repository.getById(id)
                .map(product -> product.getPrice() * product.getCount())
                .orElseThrow(() -> new IllegalArgumentException
                        ("The base does not contain a phone with this id - " + id));
    }

    public Boolean productPriceMiddleBounds(String id, double lowPrice, double highPrice) {
        return repository.getById(id).filter(product -> product.getPrice() >= lowPrice)
                .filter(product -> product.getPrice() < highPrice)
                .isPresent();
    }

    public T getProductWithIdOrCreatedIfProductMiss(String id) {
        return repository.getById(id).or(() -> Optional.of(createProductWithId(id)))
                .orElseThrow(() -> new NullPointerException("Can not return the product"));
    }

    public void printProductsExpensiveThen(double price) {
        System.out.println("Prises more then - " + price + " :");
        repository.getAll().stream()
                .filter(product -> product.getPrice() > price)
                .forEach(product -> System.out.println(product.getTitle()));
    }

    public int sumCounts() {
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

    public T createProductFromFile(String file) {
        return createProductFromMap(
                convertStringsToObjectParameters(
                        getParametersFromFile(file)));
    }

    public Map<String, String> getParametersFromFile(String file) {
        Map<String, String> parameters = new HashMap<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream(file);
        parameters.put("productType", getProductType(file));
        if (file.endsWith(".xml")) {
            getParametersFromXMLFile(input, parameters);
        } else if (file.endsWith(".json")) {
            getParametersFromJSONFile(input, parameters);
        } else {
            throw new IllegalArgumentException("Unknown file format");
        }
        return parameters;
    }

    public static String getProductType(String file) {
        final String upperFileName = file.toUpperCase();
        if (upperFileName.contains("PHONE")) {
            return "PHONE";
        } else if (upperFileName.contains("TOASTER")) {
            return "TOASTER";
        } else if (upperFileName.contains("TV")) {
            return "TV";
        } else {
            throw new IllegalArgumentException("Name of file has not correct type of product");
        }
    }

    private void getParametersFromXMLFile(InputStream inputStream, Map<String, String> parameters) {
        final Pattern patternXMLTags = Pattern.compile("<.+>(.+)</(.+)>");
        final Pattern patternInsideXMLTags = Pattern.compile("<.* (\\w+) ?=\\s?\"(.+)\">");
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final Matcher matcherXMLTags = patternXMLTags.matcher(line);
                final Matcher matcherInsideXMLTags = patternInsideXMLTags.matcher(line);
                if (matcherXMLTags.find()) {
                    parameters.put(matcherXMLTags.group(2), matcherXMLTags.group(1));
                }
                if (matcherInsideXMLTags.find()) {
                    parameters.put(matcherInsideXMLTags.group(1), matcherInsideXMLTags.group(2));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getParametersFromJSONFile(InputStream inputStream, Map<String, String> parameters) {
        final Pattern patternJSON = Pattern.compile("\"(.+)\": ?\"(.+)\"");
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                final Matcher matcherJSON = patternJSON.matcher(line);
                if (matcherJSON.find()) {
                    parameters.put(matcherJSON.group(1), matcherJSON.group(2));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
