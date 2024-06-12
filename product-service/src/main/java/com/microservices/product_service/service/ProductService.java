package com.microservices.product_service.service;

import com.microservices.product_service.dto.ProductRequest;
import com.microservices.product_service.dto.ProductResponse;
import com.microservices.product_service.model.Product;
import com.microservices.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ProductService {


    @Autowired
    ProductRepository productRepository;
    public void createProduct(ProductRequest productRequest){
         Product product = Product.builder()
                 .name(productRequest.getName())
                 .price(productRequest.getPrice())
                 .description(productRequest.getDescription())
                 .build();
         productRepository.save(product);
         System.out.println("Product " + product.getId() +" is saved");
    }



    public List<ProductResponse> getAllProducts(){

        List <Product> products = (List<Product>) productRepository.findAll();

        List<ProductResponse> productResponses = new ArrayList<>();

        for(Product p : products ){
            ProductResponse productResponse = ProductResponse.builder()
                    .name(p.getName())
                    .id(p.getId())
                    .description(p.getDescription())
                    .price(p.getPrice())
                    .build();
            productResponses.add(productResponse);
        }

        return productResponses;
    }

}
