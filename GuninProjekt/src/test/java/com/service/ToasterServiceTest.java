package com.service;

import com.model.product.Manufacturer;
import com.model.product.Toaster;
import com.repository.ToasterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

class ToasterServiceTest {

    private ToasterService target;
    private ToasterRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ToasterRepository.class);
        target = new ToasterService(repository);
    }

    @Test
    void createProduct() {
        Assertions.assertNotNull(target.createProduct());
    }

    @Test
    void getProductWithModifiedId() {
        final Toaster toaster = target.createProductWithId("000");
        Mockito.when(repository.findById("000")).thenReturn(Optional.of(toaster));
        Toaster modifiedToaster = target.getProductWithModifiedId("000", "123");
        Assertions.assertEquals("123", modifiedToaster.getId());
        Assertions.assertEquals(toaster.getTitle(), modifiedToaster.getTitle());
        Assertions.assertEquals(toaster.getPrice(), modifiedToaster.getPrice());
    }

    @Test
    void getProductWithModifiedId_throwing() {
        Mockito.when(repository.findById("123")).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.getProductWithModifiedId("123", "321"));
    }

    @Test
    void createDefaultProduct() {
        Toaster actual = target.createDefaultProduct();
        Assertions.assertEquals("Custom", actual.getTitle());
        Assertions.assertEquals(0, actual.getCount());
        Assertions.assertEquals(0.0, actual.getPrice());
        Assertions.assertEquals("Model", actual.getModel());
        Assertions.assertEquals(0, actual.getPower());
        Assertions.assertEquals(Manufacturer.SONY, actual.getManufacturer());
    }

    @Test
    void createProductWithId() {
        Toaster actual = target.createProductWithId("123");
        Assertions.assertEquals("123", actual.getId());
        Assertions.assertEquals("Custom", actual.getTitle());
        Assertions.assertEquals(0, actual.getCount());
        Assertions.assertEquals(0.0, actual.getPrice());
        Assertions.assertEquals("Model", actual.getModel());
        Assertions.assertEquals(0, actual.getPower());
        Assertions.assertEquals(Manufacturer.SONY, actual.getManufacturer());
    }
}
