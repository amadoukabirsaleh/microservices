package com.microservices.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;



@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductRequest {


    private String name;
    private String description;
    private BigDecimal price;

}
