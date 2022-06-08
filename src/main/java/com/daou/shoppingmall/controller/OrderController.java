package com.daou.shoppingmall.controller;

import com.daou.shoppingmall.dto.PurchaseDto;
import com.daou.shoppingmall.service.OrderService;
import com.daou.shoppingmall.service.impl.OrderWrapperServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1.0/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderWrapperServiceImpl orderWrapperService;

    /**
     * 구매하기 API
     * @param purchaseDto
     */
    @PostMapping
    public void purchase(@RequestBody @Valid PurchaseDto purchaseDto)  {
        log.info("New purchase registration. purchase info : ", purchaseDto);
        OrderService orderService = orderWrapperService.getServiceOf(purchaseDto);
        orderService.paymentOf(purchaseDto);
    }
}
