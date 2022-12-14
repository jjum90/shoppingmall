package com.daou.shoppingmall.entity;

import com.daou.shoppingmall.dto.DiscountContext;
import com.daou.shoppingmall.dto.PurchaseDto;
import com.daou.shoppingmall.utils.PayType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문 엔터티
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "orders")
public class Order extends BaseEntity implements Serializable {
    @Id
    @SequenceGenerator(name = "order_id_sequence", sequenceName = "order_id_sequence", initialValue = 1, allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_sequence")
    @Column(name = "ORDER_ID")
    private Long id;

    @BatchSize(size = 1000)
    @OneToMany(mappedBy = "order")
    @Column(name = "POINT_HIS")
    private List<PointHistory> pointHistories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUPON_ID")
    private Coupon coupon;

    private BigDecimal mileage;

    @Column(name = "PAYMENT")
    private BigDecimal payment;

    @BatchSize(size = 1000)
    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProduct = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private PayType payType;

    public static Order save(DiscountContext context, Member member, PurchaseDto purchaseDto, OrderStatus status, PayType payType) {
        return Order.builder()
                .createdDate(LocalDateTime.now())
                .member(member)
                .payment(purchaseDto.getTotalAmount())
                .orderStatus(status)
                .payType(payType)
                .pointHistories(context.getPointHistories())
                .mileage(context.getMileage())
                .build();
    }

    public static Order save(Member member, PurchaseDto purchaseDto, OrderStatus status, PayType payType) {
        return Order.builder()
                .createdDate(LocalDateTime.now())
                .member(member)
                .payment(purchaseDto.getTotalAmount())
                .orderStatus(status)
                .payType(payType)
                .build();
    }

    public static Order save(Coupon coupon, Member member, PurchaseDto purchaseDto, OrderStatus status, PayType payType) {
        return Order.builder()
                .createdDate(LocalDateTime.now())
                .coupon(coupon)
                .member(member)
                .payment(purchaseDto.getTotalAmount())
                .orderStatus(status)
                .payType(payType)
                .build();
    }

    public static Order save(DiscountContext context, Coupon coupon, Member member, OrderStatus status, PayType payType) {
        return Order.builder()
                .coupon(coupon)
                .member(member)
                .createdDate(LocalDateTime.now())
                .payment(context.getTotalPayAmount())
                .mileage(context.getMileage())
                .orderStatus(status)
                .payType(payType)
                .build();
    }

    public void addPaymentHistory(List<PointHistory> pointHistories) {
        this.getPointHistories().addAll(pointHistories);
    }
}
