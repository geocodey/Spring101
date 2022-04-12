package com.siku.storz.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreManagerPK implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private Manager manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", referencedColumnName = "id")
    private Store store;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreManagerPK)) return false;
        StoreManagerPK that = (StoreManagerPK) o;
        return Objects.equals(manager, that.manager) &&
                Objects.equals(store, that.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(manager, store);
    }
}