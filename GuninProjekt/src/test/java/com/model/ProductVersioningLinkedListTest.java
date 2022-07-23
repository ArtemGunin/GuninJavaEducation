package com.model;

import com.model.product.Manufacturer;
import com.model.product.Product;
import com.model.product.TV;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class ProductVersioningLinkedListTest {

    private ProductVersioningLinkedList<Product> target;
    private TV tv;

    @BeforeEach
    void setUp() {
        target = new ProductVersioningLinkedList<>();
        tv = new TV("Custom", 0, 0.0, "Model", Manufacturer.SONY, 10);
    }

    @Test
    void addNewVersion_OneElement() {
        target.addNewVersion(tv, 0);

        Assertions.assertEquals(1, target.size());
    }

    @Test
    void addNewVersion_FiveElements() {
        target.addNewVersion(tv, 1);
        target.addNewVersion(tv, 2);
        target.addNewVersion(tv, 3);
        target.addNewVersion(tv, 4);
        target.addNewVersion(tv, 5);

        Assertions.assertEquals(5, target.size());
    }

    @Test
    void addNewVersion_ContainedVersion() {
        target.addNewVersion(tv, 0);

        Assertions.assertThrows(IllegalArgumentException.class, () -> target.addNewVersion(tv, 0));
    }

    @Test
    void addNewVersion_LowVersion() {
        target.addNewVersion(tv, 10);

        Assertions.assertThrows(IllegalArgumentException.class, () -> target.addNewVersion(tv, 5));
    }

    @Test
    void findProductElementByVersion() {
        target.addNewVersion(tv, 0);

        Assertions.assertEquals(tv, target.findProductElementByVersion(0).product);
    }

    @Test
    void findProductElementByVersion_IncorrectVersion() {
        target.addNewVersion(tv, 0);

        Assertions.assertThrows(IllegalArgumentException.class, () -> target.findProductElementByVersion(1));
    }

    @Test
    void findProductByVersion() {
        target.addNewVersion(tv, 0);

        Assertions.assertEquals(tv, target.findProductByVersion(0));
    }

    @Test
    void findProductByVersion_OtherVersion() {
        target.addNewVersion(tv, 0);
        target.addNewVersion(new TV("Custom", 0, 0.0, "Model", Manufacturer.SONY, 10), 1);

        Assertions.assertNotEquals(tv, target.findProductByVersion(1));
    }

    @Test
    void findProductByVersion_IncorrectVersion() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> target.findProductByVersion(4));
    }

    @Test
    void deleteProductByVersion() {
        target.addNewVersion(tv, 1);
        target.addNewVersion(tv, 2);
        target.addNewVersion(tv, 3);

        target.deleteProductByVersion(3);

        Assertions.assertEquals(2, target.size());
    }

    @Test
    void deleteProductByVersion_IncorrectVersion() {
        target.addNewVersion(tv, 1);
        target.addNewVersion(tv, 2);
        target.addNewVersion(tv, 3);

        Assertions.assertThrows(IllegalArgumentException.class, () -> target.deleteProductByVersion(4));
    }

    @Test
    void updateProductByVersion_EqualNewProduct() {
        target.addNewVersion(tv, 0);
        TV newTV = new TV("Custom", 0, 0.0, "Model", Manufacturer.SONY, 10);
        target.updateProductByVersion(newTV, 0);

        Assertions.assertEquals(newTV, target.findProductByVersion(0));
    }

    @Test
    void updateProductByVersion_NotEqualNewProduct() {
        target.addNewVersion(tv, 0);
        TV newTV = new TV("Custom", 0, 0.0, "Model", Manufacturer.SONY, 10);
        target.updateProductByVersion(newTV, 0);

        Assertions.assertNotEquals(tv, target.findProductByVersion(0));
    }

    @Test
    void updateProductByVersion_IncorrectVersion() {
        target.addNewVersion(tv, 0);
        TV newTV = new TV("Custom", 0, 0.0, "Model", Manufacturer.SONY, 10);

        Assertions.assertThrows(IllegalArgumentException.class, () -> target.updateProductByVersion(newTV, 1));
    }

    @Test
    void getVersionsCount_FiveElementsAdded() {
        target.addNewVersion(tv, 1);
        target.addNewVersion(tv, 2);
        target.addNewVersion(tv, 3);
        target.addNewVersion(tv, 4);
        target.addNewVersion(tv, 5);

        Assertions.assertEquals(5, target.getVersionsCount());
    }

    @Test
    void getVersionsCount_ThreeAddedTwoRemoved() {
        target.addNewVersion(tv, 1);
        target.addNewVersion(tv, 2);
        target.addNewVersion(tv, 3);
        target.deleteProductByVersion(1);
        target.deleteProductByVersion(2);

        Assertions.assertEquals(1, target.size());
    }

    @Test
    void getFirstVersionDate() throws InterruptedException {
        LocalDateTime beforeFirstAdded = LocalDateTime.now();
        Thread.sleep(100);
        target.addNewVersion(tv, 0);
        Thread.sleep(100);
        LocalDateTime afterFirstAdded = LocalDateTime.now();
        target.addNewVersion(tv, 1);
        target.addNewVersion(tv, 2);

        Assertions.assertTrue(beforeFirstAdded.isBefore(target.getFirstVersionDate())
                && afterFirstAdded.isAfter(target.getFirstVersionDate()));
    }

    @Test
    void getFirstVersionDate_IncorrectVersion() throws InterruptedException {
        target.addNewVersion(tv, 0);
        target.addNewVersion(tv, 1);
        LocalDateTime beforeFirstAdded = LocalDateTime.now();
        Thread.sleep(100);
        target.addNewVersion(tv, 2);
        Thread.sleep(100);
        LocalDateTime afterFirstAdded = LocalDateTime.now();

        Assertions.assertFalse(beforeFirstAdded.isBefore(target.getFirstVersionDate())
                && afterFirstAdded.isAfter(target.getFirstVersionDate()));
    }

    @Test
    void getLastVersionDate() throws InterruptedException {
        target.addNewVersion(tv, 0);
        target.addNewVersion(tv, 1);
        LocalDateTime beforeLastAdded = LocalDateTime.now();
        Thread.sleep(100);
        target.addNewVersion(tv, 2);
        Thread.sleep(100);
        LocalDateTime afterLastAdded = LocalDateTime.now();

        Assertions.assertTrue(beforeLastAdded.isBefore(target.getLastVersionDate())
                && afterLastAdded.isAfter(target.getLastVersionDate()));
    }

    @Test
    void getLastVersionDate_IncorrectVersion() throws InterruptedException {
        LocalDateTime beforeLastAdded = LocalDateTime.now();
        Thread.sleep(100);
        target.addNewVersion(tv, 0);
        Thread.sleep(100);
        LocalDateTime afterLastAdded = LocalDateTime.now();
        target.addNewVersion(tv, 1);
        target.addNewVersion(tv, 2);

        Assertions.assertFalse(beforeLastAdded.isBefore(target.getLastVersionDate())
                && afterLastAdded.isAfter(target.getLastVersionDate()));
    }

    @Test
    void size_OneElement() {
        target.addNewVersion(tv, 0);

        Assertions.assertEquals(1, target.size());

    }

    @Test
    void size_ThreeElements() {
        target.addNewVersion(tv, 0);
        target.addNewVersion(tv, 1);
        target.addNewVersion(tv, 2);

        Assertions.assertEquals(3, target.size());

    }

}
