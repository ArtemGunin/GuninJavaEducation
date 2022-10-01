package com.model;

import com.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Invoice {
    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;
    private double sum;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "Invoice_Products",
            joinColumns = {@JoinColumn(name = "invoice", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "product", referencedColumnName = "id")}
    )
    @Transient
    private transient List<Product> products = new ArrayList<>();
    private List<String> productIds;
    private LocalDateTime time;

    @Override
    public String toString() {
        return "\nInvoice{" +
                "id='" + id + '\'' +
                ", sum=" + sum +
                ", products=" + products.toString() +
                ", time=" + time +
                '}';
    }
}
