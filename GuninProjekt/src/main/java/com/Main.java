package com;

import com.model.ProductComparator;
import com.model.ProductVersioningLinkedList;
import com.model.product.Manufacturer;
import com.model.product.Phone;
import com.model.product.Toaster;
import com.repository.PhoneRepository;
import com.repository.TVRepository;
import com.repository.ToasterRepository;
import com.service.PhoneService;
import com.service.TVService;
import com.service.ToasterService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Main {
    private static final PhoneService PHONE_SERVICE = new PhoneService(new PhoneRepository());
    private static final ToasterService TOASTER_SERVICE = new ToasterService(new ToasterRepository());
    private static final TVService TV_SERVICE = new TVService(new TVRepository());
    private static final ProductVersioningLinkedList<Phone> productVersioningLinkedList
            = new ProductVersioningLinkedList<>();
    private static final Random random = new Random();

    public static void main(String[] args) {

        PHONE_SERVICE.createAndSaveProducts(10);
        PHONE_SERVICE.printAll();
        TOASTER_SERVICE.createAndSaveProducts(10);
        TOASTER_SERVICE.printAll();
        TV_SERVICE.createAndSaveProducts(10);
        TV_SERVICE.printAll();

        Toaster updatedToaster = TOASTER_SERVICE.useProductWithIndex(3);
        updatedToaster.setCount(20);
        updatedToaster.setPrice(385);
        updatedToaster.setTitle("Title-105");
        TOASTER_SERVICE.updateProduct(updatedToaster);
        TOASTER_SERVICE.printAll();

        TV_SERVICE.deleteProduct(TV_SERVICE.useProductWithIndex(5));
        TV_SERVICE.printAll();

        PHONE_SERVICE.createAndSaveProducts(2);
        PHONE_SERVICE.printAll();
        System.out.println("\n\nAdd\n");
        PHONE_SERVICE.createAndSaveProducts(1);
        PHONE_SERVICE.printAll();

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
        for (Phone element : productVersioningLinkedList) {
            System.out.println(element);
        }
        System.out.println("\n");
        System.out.println(productVersioningLinkedList.findProductByVersion(20));
        System.out.println("\n");
        productVersioningLinkedList.deleteProductByVersion(20);
        System.out.println("\n");
        productVersioningLinkedList.updateProductByVersion(phone, 15);
        for (Phone element :
                productVersioningLinkedList) {
            System.out.println(element);
        }
        System.out.println("\n");
        for (Phone element :
                productVersioningLinkedList) {
            System.out.println(element.getModel());
        }
        System.out.println("\n");
        System.out.println(productVersioningLinkedList.getVersionsCount());
        System.out.println("\n");
        System.out.println(productVersioningLinkedList.getFirstVersionDate());
        System.out.println("\n");
        System.out.println(productVersioningLinkedList.getLastVersionDate());
        System.out.println("\n");

        List<Phone> arrayListPhones = new ArrayList<>(PHONE_SERVICE.getAll());
        Comparator<Phone> comparator = new ProductComparator<>();
        System.out.println("Not sorted");
        arrayListPhones.forEach(System.out::println);
        arrayListPhones.sort(comparator);
        System.out.println("\n");
        System.out.println("Different price");
        arrayListPhones.forEach(System.out::println);
        arrayListPhones.forEach(x -> {
            char newTitle = (char) (65 + random.nextInt(26));
            x.setTitle((Character.toString(newTitle)));
            x.setPrice(0);
        });
        System.out.println("\n");
        System.out.println("Different title");
        arrayListPhones.sort(comparator);
        arrayListPhones.forEach(System.out::println);
        arrayListPhones.forEach(x -> x.setTitle(""));
        System.out.println("\n");
        System.out.println("Different count");
        arrayListPhones.sort(comparator);
        arrayListPhones.forEach(System.out::println);
    }
}
