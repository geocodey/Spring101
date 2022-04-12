package com.siku.storz.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends AbstractEntity {

    @Id
    @GeneratedValue(generator = "store_sequence_generator")
    @SequenceGenerator(name = "store_sequence_generator",
            sequenceName="store_sequence", allocationSize = 1)
    private int id;

    @Column(nullable = false, length = 50, insertable = true)
    private String name;

    @Column(nullable = false, length = 50, insertable = true)
    private String location;

    @OneToMany(mappedBy = "store", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Section> sections;

    public Store(String name,String location,Set<Manager> storeManagers){
        this.name = name;
        this.location = location;
        this.storeManagers = storeManagers;
    }

    @PrePersist
    public void beforeSave() {
        log.info("Saving the Store {}", getName());
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "StoreManager",
            joinColumns = {
                    // navigating from the 'StoreManager' to the 'Store'
                    @JoinColumn(name = "store_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    // navigating from the 'StoreManager' to the 'Manager'
                    @JoinColumn(name = "manager_id", referencedColumnName = "id")
            }
    )
    private Set<Manager> storeManagers;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Store)) return false;
        Store store = (Store) o;
        return id == store.id &&
                Objects.equals(name, store.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
