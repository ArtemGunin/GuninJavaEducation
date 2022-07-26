package com.verification;

import com.model.ProductComparator;
import com.model.ProductVersioningLinkedList;
import com.model.product.Manufacturer;
import com.model.product.Phone;
import com.service.PhoneService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class VersioningOperations {
    private static final PhoneService PHONE_SERVICE = PhoneService.getInstance();
    private static final ProductVersioningLinkedList<Phone> productVersioningLinkedList
            = new ProductVersioningLinkedList<>();
    private static final Random random = new Random();

    public void versioning() {
        productVersioningLinkedList
                .addNewVersion(new Phone("Custom15", 0, 0.0, "Model", Manufacturer.SONY),
                        15);
        productVersioningLinkedList
                .addNewVersion(new Phone("Custom20", 0, 0.0, "Model", Manufacturer.SONY),
                        20);
        productVersioningLinkedList
                .addNewVersion(new Phone("Custom25", 0, 0.0, "Model", Manufacturer.SONY),
                        25);
        Phone phone = new Phone("NewCustom", 0, 0.0, "Model", Manufacturer.SONY);
        System.out.println("Three added products:");
        for (Phone element : productVersioningLinkedList) {
            System.out.println(element);
        }
        System.out.println("\nFounded product by version:");
        System.out.println(productVersioningLinkedList.findProductByVersion(20));
        productVersioningLinkedList.deleteProductByVersion(20);
        System.out.println("\nProducts without deleted:");
        for (Phone element : productVersioningLinkedList) {
            System.out.println(element);
        }
        productVersioningLinkedList.updateProductByVersion(phone, 15);
        System.out.println("\nUpdated products:");
        for (Phone element :
                productVersioningLinkedList) {
            System.out.println(element);
        }
        System.out.println("\nUsing foreach:");
        for (Phone element :
                productVersioningLinkedList) {
            System.out.println(element.getTitle());
        }
        System.out.println("\nCount of versions:");
        System.out.println(productVersioningLinkedList.getVersionsCount());
        System.out.println("\nFirst version date:");
        System.out.println(productVersioningLinkedList.getFirstVersionDate());
        System.out.println("\nLast version date:");
        System.out.println(productVersioningLinkedList.getLastVersionDate());

        PHONE_SERVICE.createAndSaveProducts(10);
        List<Phone> arrayListPhones = new ArrayList<>(PHONE_SERVICE.getAll());
        Comparator<Phone> comparator = new ProductComparator<>();
        System.out.println("\nNot sorted:");
        arrayListPhones.forEach(System.out::println);
        arrayListPhones.sort(comparator);
        System.out.println("\nDifferent price:");
        arrayListPhones.forEach(System.out::println);
        arrayListPhones.forEach(x -> {
            char newTitle = (char) (65 + random.nextInt(26));
            x.setTitle((Character.toString(newTitle)));
            x.setPrice(0);
        });
        System.out.println("\nDifferent title:");
        arrayListPhones.sort(comparator);
        arrayListPhones.forEach(System.out::println);
        arrayListPhones.forEach(x -> x.setTitle(""));
        System.out.println("\nDifferent count:");
        arrayListPhones.sort(comparator);
        arrayListPhones.forEach(System.out::println);
    }
}
