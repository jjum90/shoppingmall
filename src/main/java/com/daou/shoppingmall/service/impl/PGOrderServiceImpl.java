package com.daou.shoppingmall.service.impl;

import com.daou.shoppingmall.dto.DiscountContext;
import com.daou.shoppingmall.dto.PurchaseDto;
import com.daou.shoppingmall.entity.Member;
import com.daou.shoppingmall.entity.Order;
import com.daou.shoppingmall.entity.OrderStatus;
import com.daou.shoppingmall.repository.MemberRepository;
import com.daou.shoppingmall.repository.OrderRepository;
import com.daou.shoppingmall.service.DiscountService;
import com.daou.shoppingmall.service.OrderService;
import com.daou.shoppingmall.utils.PayType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * PG사 직접 결제 구현 서비스
 */
@Service
@RequiredArgsConstructor
public class PGOrderServiceImpl implements OrderService {
    private final DiscountService discountService;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void paymentOf(PurchaseDto purchaseDto) {
        Optional<Member> optMember = memberRepository.findById(Long.valueOf(purchaseDto.getMemberId()));

        if(!optMember.isPresent()) {
            throw new IllegalStateException("Not found member ny id " + purchaseDto.getMemberId());
        }
        Member member = optMember.get();
        discountService.discountProcessor(member, purchaseDto, this);
        Order order = Order.save(member, purchaseDto, OrderStatus.COMPLETE, PayType.PG);
        orderRepository.save(order);
    }

    @Override
    public void refundOf(String orderId) {

    }

    @Override
    public boolean isSatisfied(PurchaseDto purchaseDto) {
        return PayType.PG.name().equals(purchaseDto.getPayType());
    }

    @Override
    public DiscountContext processDiscount(DiscountContext context, Member member, PurchaseDto purchaseDto) {
        return context;
    }

}
