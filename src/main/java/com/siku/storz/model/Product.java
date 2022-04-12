package com.siku.storz.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.Objects;

@Getter
@Setter
@Entity(name = "Product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends AbstractEntity {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("####.##");

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "product_sequence_generator")
    @SequenceGenerator(name = "product_sequence_generator", sequenceName = "product_sequence", allocationSize = 1)
    private int id;

    @Column(name = "name", unique = true, nullable = false, insertable = true, updatable = false, length = 50)
    private String name;

    @Column(name = "price", unique = false, nullable = false, insertable = true, updatable = true, length = 10)
    private double price;

    @ManyToOne(targetEntity = Section.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "sectionId")
    private Section section;

    public Product(final String name, final double price) {
        this.name = name;
        this.price = price;
    }

    public Product(final String name, final double price,Section section) {
        this.name = name;
        this.price = price;
        this.section = section;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return id == product.id &&
                Objects.equals(name, product.name) &&
                Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }

    @Override
    public String toString() {
        return id + ", " + name + " [" + price + "]";
    }
}
