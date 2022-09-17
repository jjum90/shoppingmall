package com.daou.shoppingmall.service.impl;

import com.daou.shoppingmall.dto.DiscountContext;
import com.daou.shoppingmall.dto.PurchaseDto;
import com.daou.shoppingmall.entity.*;
import com.daou.shoppingmall.repository.MemberRepository;
import com.daou.shoppingmall.repository.OrderRepository;
import com.daou.shoppingmall.repository.PointHistoryRepository;
import com.daou.shoppingmall.service.OrderService;
import com.daou.shoppingmall.utils.Money;
import com.daou.shoppingmall.utils.PayType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 포인트로 결제하는 구현 서비스
 */
@Service
@RequiredArgsConstructor
public class PointOrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Override
    @Transactional
    public void paymentOf(PurchaseDto purchaseDto) {
        Optional<Member> optMember = memberRepository.findById(Long.valueOf(purchaseDto.getMemberId()));

        if(!optMember.isPresent()) {
            throw new IllegalStateException("Not found member by id " + purchaseDto.getMemberId());
        }
        Member member = optMember.get();
        DiscountContext context = discountProcessor(member, purchaseDto, this);
        Order order = Order.save(member, purchaseDto, OrderStatus.COMPLETE, PayType.PG);
        orderRepository.save(order);

        if(!CollectionUtils.isEmpty(context.getPointHistories())) {
            List<PointHistory> pointHistories = context.getPointHistories();
            for (PointHistory pointHis: pointHistories) {
                pointHistoryRepository.save(pointHis);
            }
        }
    }

    @Override
    public void refundOf(String orderId) {

    }

    @Override
    public boolean isSatisfied(PurchaseDto purchaseDto) {
        return PayType.POINT.name().equals(purchaseDto.getPayType());
    }

    @Override
    public DiscountContext processDiscount(DiscountContext context, Member member, PurchaseDto purchaseDto) {
        // --------------------------------- 2. 적용 가능 포인트 찾기---------------------------------------//
        Money disCountAmount;
        Money totalAmount = Money.wons(context.getTotalAmount());
        Money totalPayAmount = Money.wons(context.getTotalPayAmount());
        Money totalDiscount =  Money.wons(context.getTotalDiscountAmount());
        Money usePoint = Money.ZERO;
        List<Point> points = member.getPoints();
        List<PointHistory> pointHistories = new ArrayList<>();

        for (Point point: points) {
            Money balance = Money.wons(point.getBalance());
            disCountAmount = usePoint.plus(balance);

            if (!totalPayAmount.isGreaterThanOrEqual(disCountAmount)) {
                disCountAmount = disCountAmount.minus(totalDiscount);
            }
            totalDiscount = totalDiscount.plus(disCountAmount);
            pointHistories.add(PointHistory.save(point, disCountAmount));
        }
        context.setPointHistories(pointHistories);
        return DiscountContext.save(context, context.getCoupon(), totalAmount, totalDiscount);
    }
}
