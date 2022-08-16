package com.service;

import com.model.Customer;

import java.util.Random;
import java.util.UUID;

public class PersonService {
    private static final Random RANDOM = new Random();

    public Customer generateCustomer() {
        return new Customer(
                UUID.randomUUID().toString(),
                generateEmail(),
                generateAge());
    }

    private int generateAge() {
        int oldestHuman = 122;
        return RANDOM.nextInt(oldestHuman);
    }

    private String generateEmail() {
        String fullUUID = UUID.randomUUID().toString();
        int minEmailLength = 8;
        int characterCount = minEmailLength + RANDOM.nextInt(fullUUID.length() - minEmailLength);
        int beginSubstring = RANDOM.nextInt(fullUUID.length() - characterCount);
        return (fullUUID.substring(beginSubstring, characterCount + beginSubstring) + "@gmail.com");
    }
}
