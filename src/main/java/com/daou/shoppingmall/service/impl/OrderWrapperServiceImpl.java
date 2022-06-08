package com.daou.shoppingmall.service.impl;

import com.daou.shoppingmall.dto.PurchaseDto;
import com.daou.shoppingmall.service.OrderService;
import com.daou.shoppingmall.utils.PayType;
import lombok.RequiredArgsConstructor;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 주문, 결제 관리 Wrapper 서비스
 */
@Service
@RequiredArgsConstructor
public class OrderWrapperServiceImpl {
    private final Map<String, OrderService> serviceMap;

    /**
     * 결제 타입에 따라 구현 서비스를 제공
     * @param purchaseDto
     * @return OrderService
     */
    public OrderService getServiceOf(PurchaseDto purchaseDto) {
        PayType payType = PayType.fromType(purchaseDto.getPayType());
        Class<? extends OrderService> serviceType = payType.getServiceType();
        String beanName = serviceType.getSimpleName();
        beanName = beanName.substring(0, 1).toLowerCase().concat(beanName.substring(1));
        Service serviceAnnotation = serviceType.getAnnotation(Service.class);
        if (serviceAnnotation != null && !StringUtils.isBlank(serviceAnnotation.value())) {
            beanName = serviceAnnotation.value();
        }
        OrderService service = this.serviceMap.get(beanName);
        if (service == null) {
            throw new IllegalArgumentException("Cannot find the service.");
        }
        return service;
    }
}
