package com.service;

import com.model.product.Manufacturer;
import com.model.product.Phone;
import com.repository.PhoneRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

class PhoneServiceTest {

    private PhoneService target;
    private PhoneRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(PhoneRepository.class);
        target = new PhoneService(repository);
    }

    @Test
    void createProduct() {
        Assertions.assertNotNull(target.createProduct());
    }

    @Test
    void getProductWithModifiedId() {
        final Phone phone = target.createProductWithId("000");
        Mockito.when(repository.findById("000")).thenReturn(Optional.of(phone));
        Phone modifiedPhone = target.getProductWithModifiedId("000", "123");
        Assertions.assertEquals("123", modifiedPhone.getId());
        Assertions.assertEquals(phone.getTitle(), modifiedPhone.getTitle());
        Assertions.assertEquals(phone.getPrice(), modifiedPhone.getPrice());
    }

    @Test
    void getProductWithModifiedId_throwing() {
        Mockito.when(repository.findById("123")).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.getProductWithModifiedId("123", "321"));
    }

    @Test
    void createAndSaveDefaultProduct() {
        Phone actual = target.createDefaultProduct();
        Assertions.assertEquals("Custom", actual.getTitle());
        Assertions.assertEquals(0, actual.getCount());
        Assertions.assertEquals(0.0, actual.getPrice());
        Assertions.assertEquals("Model", actual.getModel());
        Assertions.assertEquals(Manufacturer.SONY, actual.getManufacturer());
    }

    @Test
    void createAndSaveProductWithId() {
        Phone actual = target.createProductWithId("123");
        Assertions.assertEquals("123", actual.getId());
        Assertions.assertEquals("Custom", actual.getTitle());
        Assertions.assertEquals(0, actual.getCount());
        Assertions.assertEquals(0.0, actual.getPrice());
        Assertions.assertEquals("Model", actual.getModel());
        Assertions.assertEquals(Manufacturer.SONY, actual.getManufacturer());
    }
}
