package com.calculator.exception;

public enum Messages {
    CONFIGURATION_SAVED ("The configurations was saved "),

    CONFIGURATION_NOT_EXIST ("The configurations not exist, use: api/configuration for a new configuration, insert: maxElements and maxWeight "),
    IMPOSSIBLE_TO_PROCEED("ATTENTION! impossible to proceed, reason:"),
    WEIGHT_MORE_THAN_100(" The weight of products is > 100 for ID: "),

    LIST_OF_SELECTED_PRODUCTS("The result of the selected products"),

    DUPLICATE_PRODUCT_ID(" Duplicate product id, insert unique ID for each element ID:"),

    PRICE_MORE_THAN_100(" The price is > 100 for ID: "),
    WRONG_SIZE_FOR_ELEMNTS_AND_MAX_WEIGHT("Max Elements need to be 15, Max Weight need to be maximum 100"),

    WRONG_SIZE_FOR_PRODUCTS("Products elements maximum 15");

    private final String message;

    Messages(String message) {
        this.message = message;
    }
    public String getMessage() { return message; }

}
