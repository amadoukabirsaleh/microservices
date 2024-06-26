package com.microservices.inventory_service.controller;


import ch.qos.logback.classic.Logger;
import com.microservices.inventory_service.dto.InventoryDto;
import com.microservices.inventory_service.model.Inventory;
import com.microservices.inventory_service.service.InventoryService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryDto> isInStock(@RequestParam List <String> skuCodes){
        return inventoryService.isInStock(skuCodes);


    }
}
