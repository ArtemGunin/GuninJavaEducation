package com;

import com.model.Toaster;
import com.repository.PhoneRepository;
import com.repository.TVRepository;
import com.repository.ToasterRepository;
import com.service.PhoneService;
import com.service.TVService;
import com.service.ToasterService;

public class Main {
    private static final PhoneService PHONE_SERVICE = new PhoneService(new PhoneRepository());
    private static final ToasterService TOASTER_SERVICE = new ToasterService(new ToasterRepository());
    private static final TVService TV_SERVICE = new TVService(new TVRepository());


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
        System.out.println("Add\n\n\n");
        PHONE_SERVICE.createAndSaveProducts(1);
        PHONE_SERVICE.printAll();
    }
}
