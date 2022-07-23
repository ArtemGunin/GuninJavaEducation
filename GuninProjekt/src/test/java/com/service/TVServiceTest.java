package com.service;

import com.model.product.Manufacturer;
import com.model.product.TV;
import com.repository.TVRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

class TVServiceTest {

    private TVService target;
    private TVRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(TVRepository.class);
        target = new TVService(repository);
    }

    @Test
    void createProduct() {
        Assertions.assertNotNull(target.createProduct());
    }

    @Test
    void getProductWithModifiedId() {
        final TV tv = target.createProductWithId("000");
        Mockito.when(repository.findById("000")).thenReturn(Optional.of(tv));
        TV modifiedPhone = target.getProductWithModifiedId("000", "123");
        Assertions.assertEquals("123", modifiedPhone.getId());
        Assertions.assertEquals(tv.getTitle(), modifiedPhone.getTitle());
        Assertions.assertEquals(tv.getPrice(), modifiedPhone.getPrice());
    }

    @Test
    void getProductWithModifiedId_throwing() {
        Mockito.when(repository.findById("123")).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.getProductWithModifiedId("123", "321"));
    }

    @Test
    void createAndSaveDefaultProduct() {
        TV actual = target.createDefaultProduct();
        Assertions.assertEquals("Custom", actual.getTitle());
        Assertions.assertEquals(0, actual.getCount());
        Assertions.assertEquals(0.0, actual.getPrice());
        Assertions.assertEquals("Model", actual.getModel());
        Assertions.assertEquals(Manufacturer.SONY, actual.getManufacturer());
        Assertions.assertEquals(0, actual.getDiagonal());
    }

    @Test
    void createAndSaveProductWithId() {
        TV actual = target.createProductWithId("123");
        Assertions.assertEquals("123", actual.getId());
        Assertions.assertEquals("Custom", actual.getTitle());
        Assertions.assertEquals(0, actual.getCount());
        Assertions.assertEquals(0.0, actual.getPrice());
        Assertions.assertEquals("Model", actual.getModel());
        Assertions.assertEquals(Manufacturer.SONY, actual.getManufacturer());
        Assertions.assertEquals(0, actual.getDiagonal());
    }
}
