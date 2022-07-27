package com.service;

import com.model.product.TV;
import com.repository.TVRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ProductContainerTest {

    private ProductService<TV> target;

    @BeforeEach
    void setUp() {
        TVRepository repository = Mockito.mock(TVRepository.class);
        target = TVService.getInstance(repository);
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
