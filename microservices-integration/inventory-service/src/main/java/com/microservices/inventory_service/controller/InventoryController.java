package com.microservices.inventory_service.controller;


import ch.qos.logback.classic.Logger;
import com.microservices.inventory_service.service.InventoryService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(InventoryController.class);
    @GetMapping("/{skuCode}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable("skuCode") String skuCode){

        logger.info("Checking stock for SKU: {}", skuCode);
        boolean inStock = inventoryService.isInStock(skuCode);
        logger.info("Stock status for SKU {}: {}", skuCode, inStock);

        return  inStock;
    }
}
