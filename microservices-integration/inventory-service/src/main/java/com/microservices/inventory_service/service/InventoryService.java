package com.microservices.inventory_service.service;

import com.microservices.inventory_service.dto.InventoryDto;
import com.microservices.inventory_service.model.Inventory;
import com.microservices.inventory_service.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryDto> isInStock(List <String> skuCodes){

        List<Inventory> inventoryList = inventoryRepository.findBySkuCodeIn(skuCodes);

        List<InventoryDto> inventoryDtoList = new ArrayList<>();
        if(inventoryList!=null){
            for(Inventory inv : inventoryList){
                InventoryDto inventoryDto = InventoryDto.builder()
                        .skuCode(inv.getSkuCode())
                        .isInStock(inv.getQuantity() > 0)
                        .build();

                inventoryDtoList.add(inventoryDto);
            }
        }

        return inventoryDtoList;

    }

}
