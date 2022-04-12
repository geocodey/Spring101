package com.siku.storz.controller;

import com.siku.storz.dto.StoreDTO;
import com.siku.storz.security.auth.HasAdminRole;
import com.siku.storz.security.auth.HasManagerRole;
import com.siku.storz.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
        path = "/store"
)
public class StoreController {
    private final StoreService storeService;

    @Autowired
    public StoreController(final StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping
    @HasAdminRole
    public ResponseEntity<?> create(@RequestBody StoreDTO storeDTO) {
        storeService.save(storeDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public StoreDTO getStore(@PathVariable final int id) {
        return storeService.get(id);
    }

    @GetMapping
    public List<StoreDTO> getAll() {
        return storeService.getAll();
    }

    @PutMapping("/{id}")
    @HasAdminRole
    public ResponseEntity<?> update(@PathVariable final int id, @RequestBody StoreDTO storeDTO) {
        storeService.update(id, storeDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @HasAdminRole
    public ResponseEntity<?> delete(@PathVariable final int id) {
        storeService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
