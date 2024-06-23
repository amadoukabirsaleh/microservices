package com.microservices.inventory_service;

import com.microservices.inventory_service.model.Inventory;
import com.microservices.inventory_service.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication implements CommandLineRunner{


	@Autowired
	private InventoryRepository inventoryRepository;
	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}



	@Override
	public void run(String... args) throws Exception {
		Inventory inventory1 = Inventory.builder()
				.skuCode("iphone_13")
				.quantity(100)
				.build();

		Inventory inventory2 = Inventory.builder()
				.skuCode("iphone_13_red")
				.quantity(0)
				.build();

		inventoryRepository.save(inventory1);
		inventoryRepository.save(inventory2);

	}
}


