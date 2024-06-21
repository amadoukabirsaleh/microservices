package com.microservices.order_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Entity
@Table(name="t_order_line_item")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class OrderLineItems {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String skuCode;
    private BigDecimal price;
    private Integer quantity;

}
