package com.daou.shoppingmall.utils;

import com.daou.shoppingmall.dto.DiscountContext;
import com.daou.shoppingmall.dto.PurchaseDto;
import com.daou.shoppingmall.entity.Member;
import com.daou.shoppingmall.service.DiscountPolicy;

public class ProcessUtil {
    public static DiscountContext getDefaultDiscountContext(Member member, PurchaseDto purchaseDto) {
        return DiscountContext.builder()
                .memberId(member.getId())
                .totalAmount(purchaseDto.getTotalAmount())
                .totalPayAmount(purchaseDto.getTotalAmount())
                .build();
    }
}
