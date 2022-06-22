package com.daou.shoppingmall.controller;

import com.daou.shoppingmall.dto.OrderDto;
import com.daou.shoppingmall.dto.PurchaseDto;
import com.daou.shoppingmall.service.OrderService;
import com.daou.shoppingmall.service.OrderWrapperService;
import com.daou.shoppingmall.service.impl.OrderWrapperServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderWrapperService orderWrapperService;

    /**
     * 구매하기 API
     * @param purchaseDto
     */
    @PostMapping
    public void purchase(@RequestBody @Valid PurchaseDto purchaseDto)  {
        log.info("New purchase registration. purchase info : {}",  purchaseDto);
        OrderService orderService = orderWrapperService.getPurchaseServiceOf(purchaseDto);
        orderService.paymentOf(purchaseDto);
    }

    /**
     * 주문 정보 조회 API
     * @return
     */
    @GetMapping
    public List<OrderDto> getOrders() {
        log.info("Get orders");
        return orderWrapperService.getOrders();
    }

    /**
     * 환불 API
     * @param orderId
     * @return
     */
    @PatchMapping(value = "/{orderId}")
    public String refund(@PathVariable String orderId) {
        log.info("Request refund by {}", orderId);
        OrderService orderService = orderWrapperService.getRefundServiceOf(orderId);
        orderService.refundOf(orderId);
        return null;
    }
}
