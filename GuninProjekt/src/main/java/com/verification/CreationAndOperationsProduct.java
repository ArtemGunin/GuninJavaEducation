package com.verification;

import com.model.product.Manufacturer;
import com.model.product.Toaster;
import com.service.PhoneService;
import com.service.TVService;
import com.service.ToasterService;

public class CreationAndOperationsProduct {
    private static final PhoneService PHONE_SERVICE = PhoneService.getInstance();
    private static final ToasterService TOASTER_SERVICE = ToasterService.getInstance();
    private static final TVService TV_SERVICE = TVService.getInstance();

    public void creationAndOperations() {
        System.out.println("\nCreate phones:");
        PHONE_SERVICE.createAndSaveProducts(10);
        System.out.println("\nPrint created phones:");
        PHONE_SERVICE.printAll();
        System.out.println("\nCreate toasters:");
        TOASTER_SERVICE.createAndSaveProducts(10);
        System.out.println("\nPrint created toasters:");
        TOASTER_SERVICE.printAll();
        System.out.println("\nCreate TVs:");
        TV_SERVICE.createAndSaveProducts(10);
        System.out.println("\nPrint created TVs:");
        TV_SERVICE.printAll();

        Toaster updatedToaster = new Toaster.ToasterBuilder()
                .setId(TOASTER_SERVICE.useProductWithIndex(3).getId())
                .setTitle("New Title")
                .setCount(10)
                .setPrice(100.0)
                .setModel("New Model")
                .setPower(500)
                .setManufacturer(Manufacturer.APPLE)
                .build();
        TOASTER_SERVICE.updateProduct(updatedToaster);
        System.out.println("\nPrint toasters with updated:");
        TOASTER_SERVICE.printAll();
        System.out.println("\nPrint toasters with deleted:");
        TV_SERVICE.deleteProduct(TV_SERVICE.useProductWithIndex(5));
        TV_SERVICE.printAll();
    }
}
