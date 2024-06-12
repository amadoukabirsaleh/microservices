package com.microservices.product_service.repository;

import com.microservices.product_service.model.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, String> {
}
