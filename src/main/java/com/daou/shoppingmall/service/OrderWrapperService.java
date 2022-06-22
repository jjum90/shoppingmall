package com.daou.shoppingmall.service;

import com.daou.shoppingmall.dto.OrderDto;
import com.daou.shoppingmall.dto.PurchaseDto;

import java.util.List;

public interface OrderWrapperService {
    OrderService getPurchaseServiceOf(PurchaseDto purchaseDto);
    OrderService getRefundServiceOf(String orderId);
    List<OrderDto> getOrders();
}
