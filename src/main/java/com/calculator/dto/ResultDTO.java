package com.calculator.dto;

import java.util.List;

public class ResultDTO {

    private String message;

   public List<ProductDTO> elements;

    public ResultDTO(List<ProductDTO> elem, String msg) {
        this.elements = elem;
        this.message = msg;
    }

    public String getMessage(){
        return this.message;
    }
    public List<ProductDTO> getElements(){
        return this.elements;
    }
}
