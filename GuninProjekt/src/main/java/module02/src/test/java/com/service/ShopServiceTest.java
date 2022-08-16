package com.service;

import com.repository.InvoiceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ShopServiceTest {

    private ShopService target;
    private InvoiceRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(InvoiceRepository.class);
        target = new ShopService(repository);
    }

    @Test
    void createAndSaveInvoices_negativeCount() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.createAndSaveInvoices(-1, "products.csv", 10000));
    }

    @Test
    void createAndSaveInvoices_zeroCount() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.createAndSaveInvoices(-1, "products.csv", 10000));
    }

    @Test
    void createAndSaveInvoices() {
        target.createAndSaveInvoices(1, "products.csv", 10000);
        Mockito.verify(repository).save(Mockito.any());
    }
}
