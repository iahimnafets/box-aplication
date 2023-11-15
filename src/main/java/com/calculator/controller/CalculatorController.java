package com.calculator.controller;

import com.calculator.dto.ConfigurationDTO;
import com.calculator.dto.ProductDTO;
import com.calculator.dto.GenericResponseDTO;
import com.calculator.exception.ApiRequestException;
import com.calculator.exception.Messages;
import com.calculator.service.CalculatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping( "/api" )
@Slf4j
public class CalculatorController {

    private final CalculatorService service;

    @Autowired
    public CalculatorController(final CalculatorService service) {
        this.service = service;
    }


    @PutMapping("/configuration")
    public ResponseEntity<GenericResponseDTO> saveConfigurations(@RequestBody ConfigurationDTO configurationDTO) {

        service.saveConfiguration(configurationDTO);

        return ResponseEntity.ok(
                GenericResponseDTO.builder()
                        .status(HttpStatus.OK)
                        .data(Map.of("configuration-ok", Messages.CONFIGURATION_SAVED.getMessage() ))
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/extract-products")
    public ResponseEntity<GenericResponseDTO> extractTheRightProducts(@RequestBody List<ProductDTO> elements) {

        return ResponseEntity.ok(
                GenericResponseDTO.builder()
                        .status(HttpStatus.OK)
                        .data(Map.of("result", service.extractTheRightProducts(elements)))
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }



}
