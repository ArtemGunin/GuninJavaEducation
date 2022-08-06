package com.service;

import com.model.product.Manufacturer;
import com.model.product.TV;
import com.repository.TVRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;

class ProductServiceTest {

    private ProductService<TV> target;
    private TVRepository repository;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(TVRepository.class);
        target = new ProductService<>(repository) {
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

            @Override
            protected TV createProductWithId(String id) {
                return new TV(id, "Custom", 0, 0.0, "Model", Manufacturer.SONY, 0);
            }

            @Override
            protected TV createDefaultProduct() {
                return new TV("Custom", 0, 0.0, "Model", Manufacturer.SONY, 0);
            }

            @Override
            protected TV getProductWithModifiedId(String originalId, String newId) {
                TV copiedTV = repository.findById(originalId).orElseThrow(()
                        -> new IllegalArgumentException("The base does not contain a product with this id - " + originalId));
                return new TV(newId,
                        copiedTV.getTitle(),
                        copiedTV.getCount(),
                        copiedTV.getPrice(),
                        copiedTV.getModel(),
                        copiedTV.getManufacturer(),
                        copiedTV.getDiagonal()
                );
            }

            @Override
            protected TV createProductFromMap(Map<String, Object> container) {
                return null;
            }

            @Override
            protected Map<String, Object> convertStringsToObjectParameters(Map<String, String> parameters) {
                return null;
            }
        };
    }

    @Test
    void createAndSaveProducts_negativeCount() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.createAndSaveProducts(-1));
    }

    @Test
    void createAndSaveProducts_zeroCount() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.createAndSaveProducts(0));
    }

    @Test
    void createAndSaveProducts() {
        target.createAndSaveProducts(2);
        Mockito.verify(repository).saveAll(Mockito.anyList());
    }

    @Test
    void saveProduct() {
        final TV tv = new TV("Title", 100, 1000.0,
                "Model", Manufacturer.APPLE, 30);
        target.saveProduct(tv);

        ArgumentCaptor<TV> argument = ArgumentCaptor.forClass(TV.class);
        Mockito.verify(repository).save(argument.capture());
        Assertions.assertEquals("Title", argument.getValue().getTitle());
    }

    @Test
    void saveProduct_zeroCount() {
        final TV phone = new TV("Title", 0, 1000.0,
                "Model", Manufacturer.APPLE, 30);
        target.saveProduct(phone);

        ArgumentCaptor<TV> argument = ArgumentCaptor.forClass(TV.class);
        Mockito.verify(repository).save(argument.capture());
        Assertions.assertEquals("Title", argument.getValue().getTitle());
        Assertions.assertEquals(-1, argument.getValue().getCount());
    }

    @Test
    void getAll() {
        target.getAll();
        Mockito.verify(repository).getAll();
    }

    @Test
    void getAll_CallReaMethod() {
        target.getAll();
        Mockito.doCallRealMethod().when(repository).getAll();
        Mockito.verify(repository).getAll();
    }

    @Test
    void printAll() {
        target.printAll();
        Mockito.verify(repository).getAll();
    }

    @Test
    void countOfPrintAllCalls() {
        target.saveProduct(new TV("Title-2", 200, 1000.0,
                "Model-2", Manufacturer.SAMSUNG, 30));
        target.getAll();
        target.printAll();
        target.printAll();
        target.printAll();
        Mockito.verify(repository, Mockito.times(4)).getAll();
    }

    @Test
    void useProductWithIndex() {
        TV tv = new TV("Title-2", 200, 1000.0,
                "Model-2", Manufacturer.SAMSUNG, 30);
        target.saveProduct(tv);
        Mockito.when(repository.getByIndex(0)).thenReturn(Optional.of(tv));
        Assertions.assertEquals(tv, target.useProductWithIndex(0));
    }

    @Test
    void useProductWithIndex_trowing() {
        Mockito.when(repository.getByIndex(0)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> target.useProductWithIndex(0));
    }

    @Test
    void deleteProduct_ArgumentMatcher() {
        final TV tv = new TV("Title-1", 300, 700.0,
                "Model-1", Manufacturer.SONY, 30);
        final TV otherTV = new TV("Title-2", 200, 1000.0,
                "Model-2", Manufacturer.SAMSUNG, 20);
        target.saveProduct(tv);
        target.saveProduct(otherTV);
        target.deleteProduct(tv);
        ArgumentMatcher<String> hasProductWithId =
                matchesPhoneId -> matchesPhoneId.equals(otherTV.getId());
        Mockito.when(repository.hasProduct(argThat(hasProductWithId))).thenReturn(true);
        Assertions.assertFalse(repository.hasProduct(tv.getId()));
        Assertions.assertTrue(repository.hasProduct(otherTV.getId()));
    }

    @Test
    void deleteProduct_ArgumentCaptor() {
        final TV tv = new TV("Title-1", 300, 700.0,
                "Model-1", Manufacturer.SONY, 30);
        final TV otherTV = new TV("Title-2", 200, 1000.0,
                "Model-2", Manufacturer.SAMSUNG, 20);
        target.saveProduct(tv);
        target.saveProduct(otherTV);
        target.deleteProduct(tv);

        ArgumentCaptor<TV> phoneArgumentCaptor = ArgumentCaptor.forClass(TV.class);
        Mockito.when(repository.update(phoneArgumentCaptor.capture())).thenReturn(true);
        Assertions.assertFalse(repository.hasProduct(tv.getId()));
    }

    @Test
    void updateProduct_throwing() {
        final TV tv = new TV("Title", 300, 700,
                "Model", Manufacturer.SAMSUNG, 30);
        final TV otherTV = new TV("Title-1", 200, 1000.0,
                "Model-1", Manufacturer.SONY, 20);
        target.saveProduct(otherTV);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.updateProduct(tv));
    }

    @Test
    void resetProductIfPresent() {
        target.resetProductIfPresent("123");
        Mockito.verify(repository).findById("123");
    }

    @Test
    void resetProductIfPresent_NotPresent() {
        target.createAndSaveProducts(2);
        List<TV> beforeReset = target.getAll();
        Mockito.when(repository.findById("123")).thenReturn(Optional.empty());
        target.resetProductIfPresent("123");
        Assertions.assertEquals(beforeReset, target.getAll());
    }

    @Test
    void productIsPresent_True() {
        Mockito.when(repository.findById("123")).thenReturn(Optional.of(target.createDefaultProduct()));
        Assertions.assertTrue(target.productIsPresent("123"));
    }

    @Test
    void productIsPresent_False() {
        Mockito.when(repository.findById("123")).thenReturn(Optional.empty());
        Assertions.assertFalse(target.productIsPresent("123"));
    }

    @Test
    void getProductOrCreate_GetActual() {
        final TV tv = target.createDefaultProduct();
        Mockito.when(repository.findById("123")).thenReturn(Optional.of(tv));
        Assertions.assertEquals(tv, target.getProductOrCreate("123"));
    }

    @Test
    void getProductOrCreate_Create() {
        Mockito.when(repository.findById("123")).thenReturn(Optional.empty());
        Assertions.assertEquals("123", target.getProductOrCreate("123").getId());
    }

    @Test
    void saveProductOrCreate_Save() {
        final TV tv = target.createProductWithId("123");
        target.saveProductOrCreate(tv);
        ArgumentCaptor<TV> argumentCaptor = ArgumentCaptor.forClass(TV.class);
        Mockito.verify(repository).save(argumentCaptor.capture());
        Assertions.assertEquals("123", argumentCaptor.getValue().getId());
    }

    @Test
    void saveProductOrCreate_Create() {
        target.saveProductOrCreate(null);
        ArgumentCaptor<TV> argumentCaptor = ArgumentCaptor.forClass(TV.class);
        Mockito.verify(repository).save(argumentCaptor.capture());
        Assertions.assertEquals("Custom", argumentCaptor.getValue().getTitle());
    }

    @Test
    void updatePhoneOrCreateDefault_Update() {
        final TV originalTV = target.createDefaultProduct();
        target.saveProduct(originalTV);
        final TV tv = target.createProductWithId(originalTV.getId());
        tv.setTitle("New title");
        tv.setCount(10);
        tv.setPrice(100.0);
        Mockito.when(repository.hasProduct(tv.getId())).thenReturn(true);
        Mockito.when(repository.update(tv)).thenCallRealMethod();
        Mockito.when(repository.findById(tv.getId())).thenReturn(Optional.of(originalTV));
        target.updateProductOrCreateDefault(tv);
        Assertions.assertEquals(tv.getTitle(), originalTV.getTitle());
        Assertions.assertEquals(tv.getCount(), originalTV.getCount());
        Assertions.assertEquals(tv.getPrice(), originalTV.getPrice());
    }

    @Test
    void updatePhoneOrCreateDefault_IfCreateDefault() {
        Mockito.when(repository.hasProduct("123")).thenReturn(false);
        target.updateProductOrCreateDefault(target.createDefaultProduct());
        Mockito.verify(repository).save(Mockito.any());
    }

    @Test
    void updatePhoneOrCreateDefault_CreateDefault() {
        target.updateProductOrCreateDefault(null);
        Mockito.verify(repository).save(Mockito.any());
    }

    @Test
    void getCoastOfProducts() {
        final TV tv = target.createProductWithId("123");
        Mockito.when(repository.findById("123")).thenReturn(Optional.of(tv));
        final double expected = tv.getCount() * tv.getPrice();
        Assertions.assertEquals(expected, target.getCoastOfProducts(tv.getId()));
    }

    @Test
    void getCoastOfProducts_throwing() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> target.getCoastOfProducts("123"));
    }

    @Test
    void productPriceMiddleBounds_True() {
        final TV tv = target.createProductWithId("123");
        tv.setPrice(800);
        Mockito.when(repository.findById("123")).thenReturn(Optional.of(tv));
        Assertions.assertTrue(target.productPriceMiddleBounds("123", 500, 1000));
    }

    @Test
    void productPriceMiddleBounds_False() {
        final TV tv = target.createProductWithId("123");
        Mockito.when(repository.findById("123")).thenReturn(Optional.of(tv));
        Assertions.assertFalse(target.productPriceMiddleBounds("123", 500, 1000));
    }

    @Test
    void getProductWithIdOrCreatedIfProductMiss_PhonePresent() {
        final TV tv = target.createProductWithId("123");
        Mockito.when(repository.findById("123")).thenReturn(Optional.of(tv));
        Assertions.assertEquals(tv, target.getProductWithIdOrCreatedIfProductMiss("123"));
    }

    @Test
    void getProductWithIdOrCreatedIfProductMiss_PhoneMiss() {
        final TV tv = target.createProductWithId("123");
        Mockito.when(repository.findById("123")).thenReturn(Optional.empty());
        Assertions.assertNotEquals(tv, target.getProductWithIdOrCreatedIfProductMiss("123"));
        Assertions.assertEquals("123", target.getProductWithIdOrCreatedIfProductMiss("123").getId());
    }

    @Test
    void getProductWithIdOrCreatedIfProductMiss_Trowing() {
        Mockito.when(repository.findById("123")).thenReturn(null);
        Assertions.assertThrows(NullPointerException.class,
                () -> target.getProductWithIdOrCreatedIfProductMiss("123"));
    }
}
