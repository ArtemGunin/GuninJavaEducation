package com.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonServiceTest {

    private PersonService target;

    @BeforeEach
    void setUp() {
        target = new PersonService();
    }

    @Test
    void generateCustomer_AgeLowerThen_122() {
        Assertions.assertTrue(target.generateCustomer().getAge() < 122);
    }

    @Test
    void generateCustomer_AgeNotNegative() {
        Assertions.assertTrue(target.generateCustomer().getAge() >= 0);
    }

    @Test
    void generateCustomer_CorrectEmail() {
        Assertions.assertTrue(target.generateCustomer().getEmail().endsWith("@gmail.com"));
    }

    @Test
    void generateCustomer_IdPresent() {
        Assertions.assertTrue(target.generateCustomer().getId().length() != 0);
    }

}
