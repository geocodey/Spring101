package com.siku.storz.service;

import com.siku.storz.dto.SectionDTO;
import com.siku.storz.dto.StoreDTO;
import com.siku.storz.model.Section;
import com.siku.storz.model.Store;
import com.siku.storz.repository.SectionRepository;
import com.siku.storz.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.siku.storz.dto.UtilsConvert.convertStoreDTOtoJPA;
import static com.siku.storz.dto.UtilsConvert.convertStoreToDTO;

@Service
@Slf4j
public class SectionService {

    private final SectionRepository sectionRepository;
    private final StoreService storeService;

    @Autowired
    public SectionService(final SectionRepository sectionRepository,StoreService storeService) {
        this.sectionRepository = sectionRepository;
        this.storeService = storeService;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public String save(final SectionDTO sectionDTO) {
        log.info("Saving section in db: {}", sectionDTO);

        Store store = storeService.getJPA(sectionDTO.getStore().getId());
        sectionRepository.save(new Section(sectionDTO.getName(),store));
        return "OK";
    }

    @Cacheable(cacheNames = "sections", key = "#id")
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public SectionDTO get(final int id) {
        final Section section = getByIdOrThrow(id);
        log.info("returning section:{}",section);
        return getSectionConverter().apply(section);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<SectionDTO> getAll() {
        return StreamSupport.stream(sectionRepository.findAll().spliterator(), false).filter(filterItem()).map(getSectionConverter()).sorted(Comparator.comparing(SectionDTO::getName)).collect(Collectors.toList());
    }

    @Async
    public void update(final int id, final SectionDTO sectionDTO) {
        log.info("Updating the section with the ID {}...", id);
        final Section section = getByIdOrThrow(id);
        sectionRepository.save(convertSectionForUpdate().apply(sectionDTO, section));
    }

    public void delete(final int id) {
        sectionRepository.delete(getByIdOrThrow(id));
    }

    private Section getByIdOrThrow(int id) {
        return sectionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("There is no section with the id " + id));
    }

    private Function<Section, SectionDTO> getSectionConverter() {
        return section -> new SectionDTO(section.getId(),section.getName(),convertStoreToDTO(section.getStore()));
    }

    private BiFunction<SectionDTO, Section, Section> convertSectionForUpdate() {
        return (dto, existingSection) -> {
            existingSection.setName(dto.getName());
            return existingSection;
        };
    }

    private Predicate<Section> filterItem() {
        return section -> !section.getName().isEmpty() || section.getId() < 20;
    }
}