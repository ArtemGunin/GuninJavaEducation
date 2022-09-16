package com;

import com.model.product.Manufacturer;
import com.model.product.Phone;
import com.repository.mongoDB.PhoneRepositoryDBMongo;
import com.verification.OperationsDB;

import java.io.IOException;
import java.util.Random;

public class Main {
    protected static final Random RANDOM = new Random();

    public static void main(String[] args) throws IOException {

//        new OperationsDB().run();


        PhoneRepositoryDBMongo.getInstance().save(new Phone(
                "Title-" + RANDOM.nextInt(1000),
                RANDOM.nextInt(500),
                RANDOM.nextDouble() * 1000,
                "Model-" + RANDOM.nextInt(10),
                Manufacturer.PHILIPS
        ));

    }
}
