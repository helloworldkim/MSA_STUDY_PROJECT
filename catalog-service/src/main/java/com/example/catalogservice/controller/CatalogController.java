package com.example.catalogservice.controller;

import com.example.catalogservice.jpa.CatalogEntity;
import com.example.catalogservice.service.CatalogService;
import com.example.catalogservice.vo.ResponseCatalog;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog-service")
@Slf4j
public class CatalogController {

    private final Environment env;
    private final ObjectMapper objectMapper;
    private final CatalogService catalogService;

    @GetMapping("/health_check")
    public String status() {
        return String.format("health_check UserService on PORT : %s", env.getProperty("local.server.port"));
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getAllCatalogs() {
        List<CatalogEntity> list = catalogService.getAllCatalogs();
        List<ResponseCatalog> result = new ArrayList<>();
        list.forEach(c -> result.add(objectMapper.convertValue(c, ResponseCatalog.class)));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
