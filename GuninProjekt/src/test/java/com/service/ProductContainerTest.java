package com.service;

import com.model.product.TV;
import com.repository.TVRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ProductContainerTest {

    private ProductService<TV> target;
    private TVRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(TVRepository.class);
        target = new TVService(repository) {
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
        };
    }

    @Test
    void useDiscount() {
        TV tv = target.createProduct();
        double startPrice = tv.getPrice();
        ProductContainer<TV> container = new ProductContainer<>(tv);
        container.useDiscount();
        double discountPrice = tv.getPrice();
        Assertions.assertNotEquals(startPrice, discountPrice);
    }

    @Test
    void increaseProductCount() {
        TV tv = target.createProduct();
        double startCount = tv.getCount();
        ProductContainer<TV> container = new ProductContainer<>(tv);
        container.increaseProductCount(10.2);
        double increasedCount = tv.getCount();
        Assertions.assertNotEquals(startCount, increasedCount);
    }

}
