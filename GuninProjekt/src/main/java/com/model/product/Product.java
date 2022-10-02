package com.model.product;

import com.model.Invoice;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    protected String id;
    protected String title;
    protected int count;
    protected double price;
    protected ProductType type;
    @Transient
    protected transient List<String> details;
    @ManyToOne
    @JoinColumn(name = "invoice_id")
    protected Invoice invoice;

    protected Product(String title, int count, double price, ProductType type) {
        this(UUID.randomUUID().toString(), title, count, price, type, Collections.emptyList());
    }

    protected Product(String id, String title, int count, double price, ProductType type) {
        this(id, title, count, price, type, Collections.emptyList());
    }

    protected Product(String title, int count, double price, ProductType type, List<String> details) {
        this(UUID.randomUUID().toString(), title, count, price, type, details);
    }

    protected Product(String id, String title, int count, double price, ProductType type, List<String> details) {
        this.id = id;
        this.title = title;
        this.count = count;
        this.price = price;
        this.type = type;
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
