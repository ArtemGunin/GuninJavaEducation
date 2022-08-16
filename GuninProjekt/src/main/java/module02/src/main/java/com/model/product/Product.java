package com.model.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Product {
    protected String series;
    protected ScreenType screenType;
    protected long price;
    protected ProductType productType;
}
