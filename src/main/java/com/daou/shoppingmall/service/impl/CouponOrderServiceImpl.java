package com.daou.shoppingmall.service.impl;

import com.daou.shoppingmall.dto.DiscountContext;
import com.daou.shoppingmall.dto.PurchaseDto;
import com.daou.shoppingmall.entity.*;
import com.daou.shoppingmall.repository.CouponRepository;
import com.daou.shoppingmall.repository.MemberRepository;
import com.daou.shoppingmall.repository.OrderRepository;
import com.daou.shoppingmall.service.DiscountService;
import com.daou.shoppingmall.service.OrderService;
import com.daou.shoppingmall.utils.Money;
import com.daou.shoppingmall.utils.PayType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 쿠폰 결제 구현 서비스
 */
@Service
@RequiredArgsConstructor
public class CouponOrderServiceImpl implements OrderService {
    private final DiscountService discountService;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void paymentOf(PurchaseDto purchaseDto) {
        Optional<Member> optMember = memberRepository.findById(Long.valueOf(purchaseDto.getMemberId()));
        if(!optMember.isPresent()) {
            throw new IllegalStateException("Not found member by id " + purchaseDto.getMemberId());
        }
        Member member = optMember.get();
        DiscountContext context = discountService.discountProcessor(member, purchaseDto, this);

        Coupon coupon = null;

        if(!ObjectUtils.isEmpty(context.getCoupon())) {
            Optional<Coupon> optCoupon = couponRepository.findById(context.getCoupon().getId());
            if(optCoupon.isPresent()) {
                coupon = optCoupon.get();
                coupon.setUseStatus(UseStatus.USED);
            }
        }

        Order order = Order.save(coupon, member, purchaseDto, OrderStatus.COMPLETE, PayType.COUPON);
        orderRepository.save(order);
    }

    @Override
    public void refundOf(String orderId) {

    }

    @Override
    public boolean isSatisfied(PurchaseDto purchaseDto) {
        return PayType.COUPON.name().equals(purchaseDto.getPayType());
    }

    @Override
    public DiscountContext processDiscount(DiscountContext context, Member member, PurchaseDto purchaseDto) {
        // Total Amount 계산
        Money totalAmount = Money.wons(context.getTotalAmount());
        Money disCountAmount;

        List<Coupon> coupons = member.getCoupons();
        Coupon useCoupon = null;
        for (Coupon coupon : coupons) {
            BigDecimal referencePrice = coupon.getReferencePrice();
            if (totalAmount.isGreaterThanOrEqual(Money.wons(referencePrice))) {
                useCoupon = coupon;
                break;
            }
        }
        disCountAmount = totalAmount.times(useCoupon.getDiscountRate());

        if(totalAmount.isGreaterThanOrEqual(disCountAmount)) {
            return DiscountContext.save(context, useCoupon, totalAmount, disCountAmount);
        }

        return context;
    }
}
