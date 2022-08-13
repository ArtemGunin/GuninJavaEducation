package com;

import com.command.ConsoleMenu;
import com.repository.InvoiceRepository;
import com.service.AnalyticalUtils;
import com.service.ShopService;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        ShopService shopService = new ShopService(InvoiceRepository.getInstance());

        ConsoleMenu.menu();

        //System.out.println(shopService.readFile("products.csv"));

//        shopService.readFile("products.csv")
//                .forEach(System.out::println);
//        System.out.println(ProductType.valueOf("Telephone".toUpperCase()));



//        shopService.createAndSaveInvoices(15,"products.csv", 1000);
//        AnalyticalUtils analyticalUtils = new AnalyticalUtils(InvoiceRepository.getInstance());
//        analyticalUtils.printAnalyticalData();
    }
}
