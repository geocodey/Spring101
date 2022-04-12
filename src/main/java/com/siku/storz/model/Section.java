package com.siku.storz.model;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Section")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Section extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "section_sequence_generator")
    @SequenceGenerator(name = "section_sequence_generator", sequenceName = "section_sequence",
            allocationSize = 1)
    private int id;

    @Column(name = "name", length = 40)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "storeId")
    private Store store;

    // 'mappedBy' tells the persistence provider that the join column is in the Product table
    @OneToMany(mappedBy = "section", fetch = FetchType.EAGER,cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(value = "name")
    private Set<Product> products;

    public Section(String name){
        this.name =name;
    }
    public Section(String name, Store store){
        this.name =name;
        this.store = store;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Section)) return false;
        Section that = (Section) o;
        return id == that.id &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}