package com.daou.shoppingmall.service;

import com.daou.shoppingmall.dto.DiscountContext;
import com.daou.shoppingmall.dto.PurchaseDto;
import com.daou.shoppingmall.entity.Member;

/**
 * 각 할인 정책에 따른 할인 정보 제공 인터페이스
 */
public interface DiscountPolicy {

    DiscountContext processDiscount(DiscountContext context, Member member, PurchaseDto purchaseDto);

    boolean isSatisfied(PurchaseDto purchaseDto);
}
