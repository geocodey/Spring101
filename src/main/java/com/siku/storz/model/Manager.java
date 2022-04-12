package com.siku.storz.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Manager")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Manager extends AbstractEntity {

    @Id
    private int id;

    @Column
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "StoreManager",
            joinColumns = {
                    // navigating from the 'StoreManager' to the 'Manager'
                    @JoinColumn(name = "manager_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    // navigating from the 'StoreManager' to the 'Store'
                    @JoinColumn(name = "store_id", referencedColumnName = "id")
            }
    )
    private Set<Store> stores;

    public  Manager(String name){
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Manager)) return false;
        Manager manager = (Manager) o;
        return id == manager.id &&
                Objects.equals(name, manager.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}