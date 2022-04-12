package com.siku.storz.service;


import com.siku.storz.dto.ManagerDTO;
import com.siku.storz.dto.ProductDTO;
import com.siku.storz.dto.SectionDTO;
import com.siku.storz.dto.StoreDTO;
import com.siku.storz.model.Manager;
import com.siku.storz.model.Product;
import com.siku.storz.model.Section;
import com.siku.storz.model.Store;
import com.siku.storz.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.siku.storz.dto.UtilsConvert.*;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Service
@Slf4j
public class StoreService {

    private final StoreRepository storeRepository;

    @Autowired
    public StoreService(final StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Transactional(
            readOnly = false,
            propagation = Propagation.REQUIRED
    )
    public String save(final StoreDTO storeDTO) {
        log.info("Saving store in db: {}", storeDTO);
        storeRepository.save(new Store(storeDTO.getName(), storeDTO.getLocation(), convertStoreManagersDTOtoJPA(storeDTO.getStoreManagers())));
        return "OK";
    }

    @Cacheable(cacheNames = "stores", key = "#id")
    @Transactional(
            readOnly = true,
            propagation = Propagation.SUPPORTS
    )
    public StoreDTO get(final int id) {
        final Store store = getByIdOrThrow(id);
        log.info("found store:{}",store);
        return getStoreConverter().apply(store);
    }

    public Store getJPA(final int id) {
        final Store store = getByIdOrThrow(id);
        log.info("found store:{}",store);
        return store;
    }

    @Transactional(
            readOnly = true,
            propagation = Propagation.SUPPORTS
    )
    public List<StoreDTO> getAll() {
        return StreamSupport.stream(storeRepository.findAll().spliterator(), false)
                .filter(filterItem())
                .map(getStoreConverter())
                .sorted(Comparator.comparing(StoreDTO::getName))
                .collect(Collectors.toList());
    }

    @Async
    public void update(final int id, final StoreDTO storeDTO) {
        log.debug("Updating the store with the ID {}...", id);
        final Store store = getByIdOrThrow(id);
        storeRepository.save(convertStoreForUpdate().apply(storeDTO, store));
    }

    public void delete(final int id) {
        storeRepository.delete(getByIdOrThrow(id));
    }

    private Store getByIdOrThrow(int id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("There is no store with the id " + id));
    }

    private Function<Store, StoreDTO> getStoreConverter() {
        return store -> new StoreDTO(store.getId(),store.getName(), store.getLocation(), convertStoreManagersToDTO(store.getStoreManagers()));
    }

    private BiFunction<StoreDTO, Store, Store> convertStoreForUpdate() {
        return (dto, existingStore) -> {
            existingStore.setName(dto.getName());
            return existingStore;
        };
    }

    private Predicate<Store> filterItem() {
        return store -> !store.getName().isEmpty() || store.getId() < 20;
    }
}
