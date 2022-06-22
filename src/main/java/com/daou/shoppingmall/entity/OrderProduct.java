package com.daou.shoppingmall.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 주문 상품 엔터티
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class OrderProduct extends BaseEntity implements Serializable {
    @Id
    @SequenceGenerator(name = "order_product_id_sequence", sequenceName = "order_product_id_sequence", initialValue = 1, allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_product_id_sequence")
    @Column(name = "ORDER_PRODUCT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @Column(name = "ORDER_PRICE")
    private BigDecimal orderPrice;

    @Column(name = "COUNT")
    private int count;
}
