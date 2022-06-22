package com.daou.shoppingmall.service;

import com.daou.shoppingmall.dto.PurchaseDto;

/**
 * 주문, 결제 서비스를 관리하는 인터페이스
 */
public interface OrderService extends DiscountPolicy{
    void paymentOf(PurchaseDto purchaseDto);
    void refundOf(String orderId);
}
