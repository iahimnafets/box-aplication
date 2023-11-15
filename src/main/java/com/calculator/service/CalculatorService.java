package com.calculator.service;

import com.calculator.dto.ConfigurationDTO;
import com.calculator.dto.ProductDTO;
import com.calculator.dto.ResultDTO;
import com.calculator.exception.ApiRequestException;
import com.calculator.exception.Messages;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@org.springframework.stereotype.Service
@Slf4j
public class CalculatorService {

    private static ConfigurationDTO configuration;

    public ResultDTO extractTheRightProducts(List<ProductDTO> listProducts) {
        log.info(" Begin extractTheRightProducts() input: {} ", listProducts );

        if(configuration == null){
            throw new ApiRequestException(Messages.CONFIGURATION_NOT_EXIST.getMessage());
        }
        validateInputCalculator(listProducts);

        double maxWeight = configuration.getMaxWeight();
        listProducts.sort((a, b) -> Integer.compare(b.getPrice(), a.getPrice()));

        int totalNumberProducts = listProducts.size();
        double[][] twoDimensionalArray = new double[totalNumberProducts + 1][(int) (maxWeight) + 1];

        for (int i = 1; i <= totalNumberProducts; i++) {
            for (int w = 0; w <= maxWeight; w++) {
                // Nested loops to iterate over each product and each weight
                // value to populate the  twoDimensionalArray array with intermediate results.
                double currentWeight = listProducts.get(i - 1).getWeight();
                double currentPrice = listProducts.get(i - 1).getPrice();

                //Determines the maximum value that can be achieved at the current state (i, w)
                // by considering whether the current product should be included or excluded based on its weight.
                if (currentWeight <= w) {
                     twoDimensionalArray[i][w] = Math.max( twoDimensionalArray[i - 1][(int) (w - currentWeight)] + currentPrice,  twoDimensionalArray[i - 1][w]);
                } else {
                     twoDimensionalArray[i][w] =  twoDimensionalArray[i - 1][w];
                }
            }
        }
        //Initializes variables to store the maximum price, remaining weight, and a list to store the selected product IDs.
        double maxPrice =  twoDimensionalArray[totalNumberProducts][(int) maxWeight];
        double remainingWeight = maxWeight;
        List<ProductDTO> result = new ArrayList<>();

        for (int i = totalNumberProducts; i > 0 && maxPrice > 0; i--) {
            // Iterates backward over products to determine which products
            // were selected based on the dynamic programming results.
            double currentPrice = listProducts.get(i - 1).getPrice();
            double currentWeight = listProducts.get(i - 1).getWeight();

            // Checks if the product at the current state was selected and updates
            // the result list, max price, and remaining weight accordingly.
            if (maxPrice !=  twoDimensionalArray[i - 1][(int) remainingWeight]) {
                //result.add(listProducts.get(i - 1).getId());
                result.add(listProducts.get(i - 1));
                maxPrice -= currentPrice;
                remainingWeight -= currentWeight;
            }
        }
        log.info(" End extractTheRightProducts() result: {} ", result );
        return new ResultDTO( result, Messages.LIST_OF_SELECTED_PRODUCTS.getMessage() );
    }


    private void validateInputConfigurartion(ConfigurationDTO request) {
        log.info(" Begin validateInputConfigurartion() " );
        // Check for MaxElements and MaxWeight
        if (request.getMaxElements() > 15 || request.getMaxWeight() > 100 ) {
            throw new ApiRequestException( Messages.IMPOSSIBLE_TO_PROCEED.getMessage() +
                    Messages.WRONG_SIZE_FOR_ELEMNTS_AND_MAX_WEIGHT.getMessage() );
        }
    }

    private void validateInputCalculator(List<ProductDTO> listProducts) {
        log.info(" Begin validateInputCalculator() " );

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


    public void saveConfiguration(ConfigurationDTO configurationDTO) {

        validateInputConfigurartion(configurationDTO);

        configuration = configurationDTO;
    }
}
