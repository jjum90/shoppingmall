package com.daou.shoppingmall.service;

import com.daou.shoppingmall.dto.DiscountContext;
import com.daou.shoppingmall.dto.PurchaseDto;
import com.daou.shoppingmall.entity.Member;
import com.daou.shoppingmall.utils.Money;

public interface DiscountService {
    /**
     * 할인 정책에 따른 프로세서 진행
     * @param member
     * @param purchaseDto
     * @param policies
     * @return
     */
    default DiscountContext discountProcessor(Member member, PurchaseDto purchaseDto, DiscountPolicy... policies) {
        DiscountContext context = DiscountContext.getDefaultDiscountContext(member, purchaseDto);
        Money totalAmountMoney = Money.wons(purchaseDto.getTotalAmount());
        Money totalPayAmountMoney = Money.wons(purchaseDto.getTotalAmount());

        if(totalAmountMoney.isLessThan(Money.ZERO) || totalPayAmountMoney.isEqual(Money.ZERO)) {
            return context;
        }

        for (DiscountPolicy policy: policies) {
            if(policy.isSatisfied(purchaseDto)) {
                context = policy.processDiscount(context, member, purchaseDto);
            }
        }

        return context;
    }
}
