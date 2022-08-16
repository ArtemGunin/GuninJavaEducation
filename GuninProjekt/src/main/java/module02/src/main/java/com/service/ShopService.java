package com.service;

import com.model.Customer;
import com.model.Invoice;
import com.model.InvoiceType;
import com.model.product.*;
import com.repository.InvoiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

public class ShopService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShopService.class);
    private static final Logger INVOICE_LOGGER = LoggerFactory.getLogger("logs/formedInvoices");
    private static final Random RANDOM = new Random();
    private final InvoiceRepository repository;

    public ShopService(InvoiceRepository repository) {
        this.repository = repository;
    }

    public void createAndSaveInvoices(int invoiceCount, String file, long costLimit) {
        if (invoiceCount < 1) {
            throw new IllegalArgumentException("count must been bigger then 0");
        }
        int count = invoiceCount;
        do {
            createAndSaveInvoice(file, costLimit);
            count--;
        } while (count > 0);
    }

    private void createAndSaveInvoice(String file, long costLimit) {
        List<Product> productsList = createRandomProductsList(file);
        Customer customer = new PersonService().generateCustomer();
        Invoice invoice = new Invoice(
                productsList,
                customer,
                defineInvoiceType(costLimit, productsList),
                LocalDateTime.now());
        repository.save(invoice);
        INVOICE_LOGGER.info("[" + invoice.getCreated() + "][" + customer + "][" + invoice + "]");
    }

    private InvoiceType defineInvoiceType(long costLimit, List<Product> productsList) {
        getTotalPrice(productsList);
        if (getTotalPrice(productsList) > costLimit) {
            return InvoiceType.WHOLESALE;
        }
        return InvoiceType.RETAIL;
    }

    private long getTotalPrice(List<Product> products) {
        return products.stream()
                .mapToLong(Product::getPrice)
                .sum();
    }

    private List<Product> createRandomProductsList(String file) {
        List<Product> productsListToInvoice = new LinkedList<>();
        int minProductCount = 1;
        List<Map<String, String>> productsFromFile = readFile(file);
        int productsCount = minProductCount + RANDOM.nextInt(5);
        for (int i = productsCount; i > 0; i--) {
            productsListToInvoice.add(randomProduct(productsFromFile));
        }
        return productsListToInvoice;
    }

    private Product randomProduct(List<Map<String, String>> productsFromFile) {
        int randomProductNumber = RANDOM.nextInt(productsFromFile.size());
        return createProductFromMap(productsFromFile.get(randomProductNumber));
    }

    private List<Map<String, String>> readFile(String file) {
        List<Map<String, String>> parameters = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream(file);
        if (input != null) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input))) {
                String[] keys = bufferedReader.readLine().split(",");
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    parameters.add(parametersToMap(line.split(","), keys));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return parameters;
    }

    private Map<String, String> parametersToMap(String[] valuesFromLine, String[] keys) {
        Map<String, String> parameters = new HashMap<>();
        for (int i = 0; i < valuesFromLine.length; i++) {
            try {
                if (valuesFromLine[i].equals("")) {
                    throw new IncorrectLineException("Incorrect line! Miss parameter: " + keys[i] + ", in line : " + Arrays.toString(valuesFromLine));
                }
                parameters.put(keys[i], valuesFromLine[i]);
            } catch (IncorrectLineException e) {
                LOGGER.debug(e.getMessage());
            }
        }
        return parameters;
    }

    private Product createProductFromMap(Map<String, String> productContainer) {
        Function<Map<String, String>, Product> product = fields ->
                switch (ProductType.valueOf(productContainer.get("type").toUpperCase())) {
                    case TELEPHONE -> new Telephone.TelephoneBuilder()
                            .setModel(fields.get("model"))
                            .setSeries(fields.get("series"))
                            .setScreenType(ScreenType.valueOf(fields.get("screen type")))
                            .setPrice(Long.parseLong(fields.get("price")))
                            .build();
                    case TELEVISION -> new Television.TelevisionBuilder()
                            .setSeries(fields.get("series"))
                            .setCountry(fields.get("country"))
                            .setScreenType(ScreenType.valueOf(fields.get("screen type").toUpperCase()))
                            .setDiagonal(Double.parseDouble(fields.get("diagonal")))
                            .setPrice(Long.parseLong(fields.get("price")))
                            .build();
                };
        return product.apply(productContainer);
    }

    private static class IncorrectLineException extends Exception {
        public IncorrectLineException(String massage) {
            super(massage);
        }
    }
}
