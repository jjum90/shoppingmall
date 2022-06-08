package com.daou.shoppingmall.service.impl;

import com.daou.shoppingmall.dto.DiscountContext;
import com.daou.shoppingmall.dto.PurchaseDto;
import com.daou.shoppingmall.entity.Coupon;
import com.daou.shoppingmall.entity.Member;
import com.daou.shoppingmall.entity.Order;
import com.daou.shoppingmall.entity.UseStatus;
import com.daou.shoppingmall.repository.CouponRepository;
import com.daou.shoppingmall.repository.MemberRepository;
import com.daou.shoppingmall.repository.OrderRepository;
import com.daou.shoppingmall.service.OrderService;
import com.daou.shoppingmall.utils.Money;
import com.daou.shoppingmall.utils.PayType;
import com.daou.shoppingmall.utils.ProcessUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 쿠폰 결제 구현 서비스
 */
@Service
@RequiredArgsConstructor
public class CouponOrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void paymentOf(PurchaseDto purchaseDto) {
        Optional<Member> optMember = memberRepository.findById(Long.valueOf(purchaseDto.getMemberId()));
        if(!optMember.isPresent()) {
            throw new IllegalStateException("Not fount Member By id " + purchaseDto.getMemberId());
        }
        Member member = optMember.get();
        DiscountContext context = ProcessUtil.getDefaultDiscountContext(member, purchaseDto);
        context = discountProcessor(new DiscountContext(), member, purchaseDto, this);

        Coupon coupon = null;

        if(!ObjectUtils.isEmpty(context.getCoupon())) {
            Optional<Coupon> optCoupon = couponRepository.findById(context.getCoupon().getId());
            if(optCoupon.isPresent()) {
                coupon = optCoupon.get();
                coupon.setUseStatus(UseStatus.USED);
            }
        }

        Order order = Order.builder()
                .createdDate(LocalDateTime.now())
                .coupon(coupon)
                .member(member)
                .payment(purchaseDto.getTotalAmount())
                .build();
        orderRepository.save(order);
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
            return DiscountContext.builder()
                    .coupon(useCoupon)
                    .totalAmount(context.getTotalAmount())
                    .totalPayAmount(totalAmount.minus(disCountAmount).getAmount())
                    .totalDiscountAmount(disCountAmount.getAmount())
                    .mileage(context.getMileage())
                    .pointHistories(context.getPointHistories())
                    .memberId(context.getMemberId())
                    .build();
        }

        return context;
    }
}
