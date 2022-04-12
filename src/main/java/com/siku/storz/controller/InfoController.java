package com.siku.storz.controller;

import com.siku.storz.dto.ProductDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {
    @GetMapping("/info")
    public String getInfo() {
        return "Welcome to my spring app";
    }

}
