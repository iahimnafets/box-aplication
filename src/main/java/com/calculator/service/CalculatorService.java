package com.calculator.service;

import com.calculator.dto.ConfigurationDTO;
import com.calculator.dto.ProductDTO;
import com.calculator.dto.ResultDTO;
import com.calculator.exception.ApiRequestException;
import com.calculator.exception.Messages;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;


@org.springframework.stereotype.Service
@Slf4j
public class CalculatorService {

    private static ConfigurationDTO configuration;

    public ResultDTO extractTheRightProducts(List<ProductDTO> listProducts) {

        if(configuration == null){
            throw new ApiRequestException(Messages.CONFIGURATION_NOT_EXIST.getMessage());
        }
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
        return new ResultDTO( result, Messages.LIST_OF_SELECTED_PRODUCTS.getMessage() );
    }



    public void saveConfiguration(ConfigurationDTO configurationDTO) {
        configuration = configurationDTO;
    }
}
