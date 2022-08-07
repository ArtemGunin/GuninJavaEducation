package com.model.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Body {
    private String material;
    private String color;

    public Body(String material, String color) {
        this.material = material;
        this.color = color;
    }
}
