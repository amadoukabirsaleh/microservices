package com.microservices.order_service.service;

import com.microservices.order_service.dto.OrderDto;
import com.microservices.order_service.dto.OrderLineItemsDto;
import com.microservices.order_service.model.Order;
import com.microservices.order_service.model.OrderLineItems;
import com.microservices.order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {



    @Autowired
    private OrderRepository orderRepository;


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

        orderRepository.save(order);
        return "Order " + order.getId() +" placed sucessfully";


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
