package com.model.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperatingSystem {
    private String designation;
    int version;

    public OperatingSystem(String designation, int version) {
        this.designation = designation;
        this.version = version;
    }
}
