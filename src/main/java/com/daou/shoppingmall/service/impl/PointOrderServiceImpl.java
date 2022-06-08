package com.daou.shoppingmall.service.impl;

import com.daou.shoppingmall.dto.DiscountContext;
import com.daou.shoppingmall.dto.PurchaseDto;
import com.daou.shoppingmall.entity.Member;
import com.daou.shoppingmall.entity.Order;
import com.daou.shoppingmall.entity.Point;
import com.daou.shoppingmall.entity.PointHistory;
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
import java.time.LocalDateTime;
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
            throw new IllegalStateException("Not fount Member By id " + purchaseDto.getMemberId());
        }
        Member member = optMember.get();
        DiscountContext context = discountProcessor(new DiscountContext(), member, purchaseDto, this);

        Order order = Order.builder()
                .createdDate(LocalDateTime.now())
                .member(member)
                .payment(purchaseDto.getTotalAmount())
                .build();
        orderRepository.save(order);

        if(!CollectionUtils.isEmpty(context.getPointHistories())) {
            List<PointHistory> pointHistories = context.getPointHistories();
            for (PointHistory pointHis: pointHistories) {
                pointHistoryRepository.save(pointHis);
            }
        }
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
        Money usePointMoney = Money.ZERO;
        Money totalDiscountMoney =  Money.wons(context.getTotalDiscountAmount());
        List<Point> points = member.getPoints();
        List<PointHistory> pointHistories = new ArrayList<>();

        for (Point point: points) {
            Money balance = Money.wons(point.getBalance());
            disCountAmount = usePointMoney.plus(balance);

            if (!totalPayAmount.isGreaterThanOrEqual(disCountAmount)) {
                disCountAmount = disCountAmount.minus(totalDiscountMoney);
            }
            totalDiscountMoney = totalDiscountMoney.plus(disCountAmount);

            pointHistories.add(
                PointHistory.builder()
                    .usePoint(disCountAmount.getAmount())
                    .createdDate(LocalDateTime.now())
                    .point(point)
                    .build()
            );
        }

        return DiscountContext.builder()
                .coupon(context.getCoupon())
                .pointHistories(pointHistories)
                .mileage(context.getMileage())
                .totalDiscountAmount(totalDiscountMoney.getAmount())
                .totalPayAmount(totalAmount.minus(totalDiscountMoney).getAmount())
                .totalAmount(context.getTotalAmount())
                .memberId(context.getMemberId())
                .build();
    }
}
