package com.siku.storz.controller;

import com.siku.storz.dto.SectionDTO;
import com.siku.storz.security.auth.HasAdminRole;
import com.siku.storz.security.auth.HasManagerRole;
import com.siku.storz.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        path = "/section"
)
public class SectionController {
    private final SectionService sectionService;

    @Autowired
    public SectionController(final SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    @HasAdminRole
    public ResponseEntity<?> create(@RequestBody SectionDTO sectionDTO) {
        sectionService.save(sectionDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public SectionDTO getSection(@PathVariable final int id) {
        return sectionService.get(id);
    }

    @GetMapping
    public List<SectionDTO> getAll() {
        return sectionService.getAll();
    }

    @PutMapping("/{id}")
    @HasAdminRole
    public ResponseEntity<?> update(@PathVariable final int id, @RequestBody SectionDTO sectionDTO) {
        sectionService.update(id, sectionDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @HasAdminRole
    public ResponseEntity<?> delete(@PathVariable final int id) {
        sectionService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
