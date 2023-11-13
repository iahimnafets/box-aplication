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

        validateInputConfigurartion(configurationDTO);
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

        validateInputCalculator(elements);

        return ResponseEntity.ok(
                GenericResponseDTO.builder()
                        .status(HttpStatus.OK)
                        .data(Map.of("result", service.extractTheRightProducts(elements)))
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    private void validateInputConfigurartion(ConfigurationDTO request) {

        // Check for MaxElements and MaxWeight
        if (request.getMaxElements() > 15 || request.getMaxWeight() > 100 ) {
            throw new ApiRequestException( Messages.IMPOSSIBLE_TO_PROCEED.getMessage() +
                    Messages.WRONG_SIZE_FOR_ELEMNTS_AND_MAX_WEIGHT.getMessage() );
        }
    }

    private void validateInputCalculator(List<ProductDTO> listProducts) {
        Set<Integer> seenIds = new HashSet<>();
        StringBuffer msgErr = new StringBuffer("");

        if (listProducts.size() > 15) {
            msgErr.append(Messages.WRONG_SIZE_FOR_PRODUCTS.getMessage());
        }
        for (ProductDTO prod : listProducts) {
            int productId = prod.getId();
            // Check for duplicate IDs
            if (!seenIds.add(productId)) {
                msgErr.append(Messages.DUPLICATE_PRODUCT_ID.getMessage() + productId);
            }
            // Check for Weight
            if (prod.getWeight() > 100) {
                msgErr.append(Messages.WEIGHT_MORE_THAN_100.getMessage() + productId);
            }
            // Check for Price
            if (prod.getPrice() > 100) {
                msgErr.append(Messages.PRICE_MORE_THAN_100.getMessage() + productId);
            }
        }
        //  Check for error messages
        if (!msgErr.toString().isEmpty()) {
            throw new ApiRequestException(Messages.IMPOSSIBLE_TO_PROCEED.getMessage() + " " + msgErr.toString());
        }
    }



}
