package com.daou.shoppingmall.utils;

import com.daou.shoppingmall.service.*;
import com.daou.shoppingmall.service.impl.*;

/**
 * 결제 타입과 우선순위를 관리하는 ENUM
 * 첫 번째 인자 - 결제 서비스
 * 두 번째 인자 - 우선 순위
 */
public enum PayType {
    AUTO(AutoOrderServiceImpl.class, 0),
    COUPON(CouponOrderServiceImpl.class, 1),
    POINT(PointOrderServiceImpl.class, 2),
    MILEAGE(MileageOrderServiceImpl.class, 3),
    PG(PGOrderServiceImpl.class, 4);

    private Class<? extends OrderService> serviceType;
    private int priority;

    public Class<? extends OrderService> getServiceType() {
        return serviceType;
    }

    public int getPriority() {
        return priority;
    }

    PayType(Class<? extends OrderService> serviceType, int priority) {
        this.serviceType = serviceType;
        this.priority = priority;
    }

    public static PayType fromType(String type) {
        for (PayType payType : values()) {
            if (payType.name().equals(type)) {
                return payType;
            }
        }
        return null;
    }

    public static <T> PayType fromOf(DiscountPolicy policy) {
        if (policy instanceof CouponOrderServiceImpl) {
            return PayType.COUPON;
        }else if (policy instanceof PointOrderServiceImpl) {
            return PayType.POINT;
        }else if (policy instanceof MileageOrderServiceImpl) {
            return PayType.MILEAGE;
        }else if (policy instanceof PGOrderServiceImpl) {
            return PayType.PG;
        }
        return PayType.AUTO;
    }
}
