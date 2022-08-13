package com.repository;

import com.model.Invoice;
import com.model.InvoiceType;
import com.model.product.Product;
import com.model.product.ScreenType;
import com.model.product.Telephone;
import com.service.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

class InvoiceRepositoryTest {

    private InvoiceRepository target;

    private Invoice invoice;

    @BeforeEach
    void setUp() {
        target = InvoiceRepository.getInstance();
        List<Product> productList = new LinkedList<>();
        productList.add(new Telephone.TelephoneBuilder()
                .setModel("model")
                .setSeries("series")
                .setScreenType(ScreenType.AMOLED)
                .setPrice(10000)
                .build());
        invoice = new Invoice(
                productList,
                new PersonService().generateCustomer(),
                InvoiceType.RETAIL,
                LocalDateTime.now());
    }

    @Test
    void save() {
        target.save(invoice);
        final List<Invoice> invoices = target.getAllToList();
        Assertions.assertEquals(1, invoices.size());
        Assertions.assertEquals(invoices.get(0).getCreated(), invoice.getCreated());
    }

    @Test
    void save_putNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.save(null));
        final List<Invoice> actualResult = target.getAllToList();
        Assertions.assertEquals(0, actualResult.size());
    }

    @Test
    void getByUserId_SomeInvoicePresent() {
        target.save(invoice);
        final Optional<Invoice> optionalInvoice = target.getByUserId(invoice.getCustomer().getId());
        Assertions.assertTrue(optionalInvoice.isPresent());
    }

    @Test
    void getByUserId_CorrectInvoicePresent() {
        target.save(invoice);
        final Optional<Invoice> optionalInvoice = target.getByUserId(invoice.getCustomer().getId());
        final Invoice actualInvoice = optionalInvoice.orElseThrow();
        Assertions.assertEquals(invoice.getCustomer().getId(), actualInvoice.getCustomer().getId());
    }

    @Test
    void getAllToList() {
        target.save(invoice);
        final List<Invoice> actualResult = target.getAllToList();
        Assertions.assertEquals(1, actualResult.size());
    }

    @Test
    void getAllToList_noPhones() {
        final List<Invoice> actualResult = target.getAllToList();
        Assertions.assertEquals(0, actualResult.size());
    }
}
