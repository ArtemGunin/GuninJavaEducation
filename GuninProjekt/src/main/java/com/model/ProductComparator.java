package com.model;

import com.model.product.Product;

import java.util.Comparator;

public class ProductComparator<T extends Product> implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
        return Comparator.comparing(Product::getPrice).reversed()
                .thenComparing(Product::getTitle)
                .thenComparing(Product::getCount)
                .compare(o1, o2);
    }
}
