package com.microservices.order_service.service;

import com.microservices.order_service.dto.InventoryDto;
import com.microservices.order_service.dto.OrderDto;
import com.microservices.order_service.dto.OrderLineItemsDto;
import com.microservices.order_service.model.Order;
import com.microservices.order_service.model.OrderLineItems;
import com.microservices.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {



    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WebClient webClient;

    private final ApplicationEventPublisher applicationEventPublisher;

    public String placeOrder(OrderDto orderDto){

        Order order = new Order();

        order.setOrderNumber(UUID.randomUUID().toString());

//        List<OrderLineItems> orderLineItems = orderDto.getOrderLineItemsDtoList()
//                .stream()
//                        .map(this::mapToDto)
//                                .toList();
//        order.setOrderLineItemsList(orderLineItems);


        for(OrderLineItemsDto orderLineItemsDto : orderDto.getOrderLineItemsDtoList()){
            OrderLineItems orderLineItems = OrderLineItems.builder()
                            .id(orderLineItemsDto.getId())
                            .skuCode(orderLineItemsDto.getSkuCode())
                            .price(orderLineItemsDto.getPrice())
                            .quantity(orderLineItemsDto.getQuantity())
                            .build();

            order.getOrderLineItemsList().add(orderLineItems);
        }

        //getting the skucodes of OrderLineItems to check them in our inventory service
        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();


        //Logic to check if all products are the stock and then if so place the order
        InventoryDto[] inventoryDtoArray =  webClient.get()
                                                .uri("http://localhost:9002/api/inventory",
                                                        uriBuilder -> uriBuilder.queryParam("skuCodes",skuCodes).build())
                                                .retrieve()
                                                .bodyToMono(InventoryDto[].class)
                                                .block();




         //Checking if all the orderLineItems are indeed in the stock

        boolean allProductsInStock = Arrays.stream(inventoryDtoArray)
                        .allMatch(InventoryDto::isInStock);

        if(allProductsInStock) {
            orderRepository.save(order);

            System.out.println("Order " + order.getId() +" placed sucessfully");
            return "Order " + order.getId() +" placed sucessfully";
        }
        else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }




    }



    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto){
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }



    public List<OrderDto> getAllOrders(){
        List<Order> orders = new ArrayList<Order>();
        orders = orderRepository.findAll();

        List<OrderDto> orderDtoList = new ArrayList<OrderDto>();
        List<OrderLineItemsDto> orderLineItemsDtoList = new ArrayList<OrderLineItemsDto>();

        for (Order order : orders){
            for(OrderLineItems orderLineItems : order.getOrderLineItemsList()){
                OrderLineItemsDto orderLineItemsDto = OrderLineItemsDto.builder()
                        .id(orderLineItems.getId())
                        .skuCode(orderLineItems.getSkuCode())
                        .price(orderLineItems.getPrice())
                        .quantity(orderLineItems.getQuantity())
                        .build();

                orderLineItemsDtoList.add(orderLineItemsDto);
            }

            OrderDto orderDto = OrderDto.builder()
                    .orderLineItemsDtoList(orderLineItemsDtoList)
                    .build();

            orderDtoList.add(orderDto);

        }


        return orderDtoList;
    }


}
