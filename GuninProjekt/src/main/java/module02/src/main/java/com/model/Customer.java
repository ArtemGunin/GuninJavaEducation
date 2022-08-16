package com.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer {
    private String id;
    private String email;
    private int age;

    public Customer(String id, String email, int age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
