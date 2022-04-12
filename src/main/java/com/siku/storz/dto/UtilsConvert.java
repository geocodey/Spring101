package com.siku.storz.dto;

import com.siku.storz.model.Manager;
import com.siku.storz.model.Product;
import com.siku.storz.model.Section;
import com.siku.storz.model.Store;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Slf4j
public class UtilsConvert {
    public static Set<Product> convertProductsDTOtoJPA(Set<ProductDTO> products) {
        return emptyIfNull(products).stream().map(productDTO -> new Product(productDTO.getName(),productDTO.getPrice())).collect(Collectors.toSet());
    }

    public static Set<ProductDTO> convertProductsToDTO(Set<Product> products) {
        return emptyIfNull(products).stream().map(product -> new ProductDTO(product.getName(),product.getPrice())).collect(Collectors.toSet());
    }

    public static Set<ManagerDTO> convertStoreManagersToDTO(Set<Manager> storeManagers) {
        return emptyIfNull(storeManagers).stream().map(manager -> new ManagerDTO(manager.getName())).collect(Collectors.toSet());
    }

    public static Set<SectionDTO> convertSectionsToDTO(Set<Section> sections) {
        return emptyIfNull(sections).stream()
                .map(s -> new SectionDTO(s.getId(),s.getName(),convertStoreToDTO(s.getStore())))
                .collect(Collectors.toSet());
    }

    public static StoreDTO convertStoreToDTO(Store store) {
        if(store == null){
            return null;
        }
        return new StoreDTO(store.getId(),store.getName(),store.getLocation(),convertStoreManagersToDTO(store.getStoreManagers()));
    }

    public static Store convertStoreDTOtoJPA(StoreDTO storeDTO) {
        if(storeDTO == null){
            return null;
        }
        return new Store(storeDTO.getName(),storeDTO.getLocation(),convertStoreManagersDTOtoJPA(storeDTO.getStoreManagers()));
    }

    public static Set<Manager> convertStoreManagersDTOtoJPA(Set<ManagerDTO> storeManagers) {
        return emptyIfNull(storeManagers).stream().map(managerDTO -> new Manager(managerDTO.getName())).collect(Collectors.toSet());
    }

    public static Set<Section> convertSectionsDTOtoJPA(Set<SectionDTO> sections) {
        return emptyIfNull(sections).stream().map(sectionDTO -> new Section(sectionDTO.getName())).collect(Collectors.toSet());
    }
}
