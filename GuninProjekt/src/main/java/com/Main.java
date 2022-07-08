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
        PHONE_SERVICE.createAndSavePhones(10);
        PHONE_SERVICE.printAll();
        TOASTER_SERVICE.createAndSaveToasters(10);
        TOASTER_SERVICE.printAll();
        TV_SERVICE.createAndSaveTVs(10);
        TV_SERVICE.printAll();

        Toaster updatedToaster = TOASTER_SERVICE.useToasterWithIndex(3);
        updatedToaster.setCount(20);
        updatedToaster.setPrice(385);
        updatedToaster.setTitle("Title-105");
        TOASTER_SERVICE.updateToaster(updatedToaster);
        TOASTER_SERVICE.printAll();

        TV_SERVICE.deleteTV(TV_SERVICE.useTVWithIndex(5));
        TV_SERVICE.printAll();
    }
}
