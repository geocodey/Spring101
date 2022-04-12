package com.siku.storz.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "StoreManager")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuppressWarnings("unused")
public class StoreManager extends AbstractEntity {

    @EmbeddedId
    private StoreManagerPK storeManagerPK;

    public StoreManagerPK getStoreManagerPK() {
        return storeManagerPK;
    }

    public void setStoreManagerPK(StoreManagerPK storeManagerPK) {
        this.storeManagerPK = storeManagerPK;
    }
}
