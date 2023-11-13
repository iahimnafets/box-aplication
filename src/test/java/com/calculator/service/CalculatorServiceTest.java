package com.calculator.service;

import com.calculator.dto.ConfigurationDTO;
import com.calculator.dto.ProductDTO;
import com.calculator.dto.ResultDTO;
import com.calculator.exception.ApiRequestException;
import com.calculator.exception.Messages;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CalculatorServiceTest {

    @Mock
    private ConfigurationDTO configurationDTO;

    @InjectMocks
    private CalculatorService calculatorService;

    @Before
    public void setUp() {
        when(configurationDTO.getMaxWeight()).thenReturn(100.0);
        calculatorService.saveConfiguration(configurationDTO);
    }

    @Test
    public void extractTheRightProducts_ValidInput_ReturnsResultDTO() {
        List<ProductDTO> products = createTestProducts();
        ResultDTO resultDTO = calculatorService.extractTheRightProducts(products);

        // I expect 3 elements after the elaborations
        assertEquals(3, resultDTO.elements.size());
        assertEquals(Messages.LIST_OF_SELECTED_PRODUCTS.getMessage(), resultDTO.getMessage());
    }

    @Test
    public void extractTheRightProducts_NoConfiguration_ThrowsApiRequestException() {
        calculatorService.saveConfiguration(null);

        ApiRequestException exception = assertThrows(ApiRequestException.class, () -> {
            calculatorService.extractTheRightProducts(createTestProducts());
        });
        assertEquals(Messages.CONFIGURATION_NOT_EXIST.getMessage(), exception.getMessage());
    }

    private List<ProductDTO> createTestProducts() {
        List<ProductDTO> products = new ArrayList<>();
        products.add(new ProductDTO(1, 50, 50));
        products.add(new ProductDTO(2, 30, 40));
        products.add(new ProductDTO(3, 20, 45));
        products.add(new ProductDTO(4, 20, 25));
        return products;
    }


}

