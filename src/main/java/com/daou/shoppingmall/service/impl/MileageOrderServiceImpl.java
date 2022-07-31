package com.daou.shoppingmall.service.impl;

import com.daou.shoppingmall.dto.DiscountContext;
import com.daou.shoppingmall.dto.PurchaseDto;
import com.daou.shoppingmall.entity.Member;
import com.daou.shoppingmall.entity.Mileage;
import com.daou.shoppingmall.entity.Order;
import com.daou.shoppingmall.entity.OrderStatus;
import com.daou.shoppingmall.repository.MemberRepository;
import com.daou.shoppingmall.repository.MileageRepository;
import com.daou.shoppingmall.repository.OrderRepository;
import com.daou.shoppingmall.service.OrderService;
import com.daou.shoppingmall.utils.Money;
import com.daou.shoppingmall.utils.PayType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 적립금 구현 서비스
 */
@Service
@RequiredArgsConstructor
public class MileageOrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository;
    private final MileageRepository mileageRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void paymentOf(PurchaseDto purchaseDto) {
        Optional<Member> optMember = memberRepository.findById(Long.valueOf(purchaseDto.getMemberId()));
        if(!optMember.isPresent()) {
            throw new IllegalStateException("Not found member by id " + purchaseDto.getMemberId());
        }
        Member member = optMember.get();
        DiscountContext context = discountProcessor(member, purchaseDto, this);

        Order order = Order.builder()
                .createdDate(LocalDateTime.now())
                .member(member)
                .payment(purchaseDto.getTotalAmount())
                .orderStatus(OrderStatus.COMPLETE)
                .payType(PayType.COUPON)
                .build();
        orderRepository.save(order);

        if(!ObjectUtils.isEmpty(context.getMileage())) {
            Optional<Mileage> optMileage = mileageRepository.findById(member.getId());
            if(optMileage.isPresent()) {
                Mileage mileage = optMileage.get();
                Money mileageMoney = Money.wons(mileage.getBalance()).minus(Money.wons(context.getMileage()));
                mileage.setBalance(mileageMoney.getAmount());
                mileageRepository.save(mileage);
            }
        }
    }

    @Override
    public void refundOf(String orderId) {

    }

    @Override
    public boolean isSatisfied(PurchaseDto purchaseDto) {
        return PayType.MILEAGE.name().equals(purchaseDto.getPayType());
    }

    @Override
    public DiscountContext processDiscount(DiscountContext context, Member member, PurchaseDto purchaseDto) {
        Money totalAmount = Money.wons(context.getTotalAmount());
        Money totalPayAmount = Money.wons(context.getTotalPayAmount());
        Money totalDiscountMoney =  Money.wons(context.getTotalDiscountAmount());

        // --------------------------------- 3. 적용 가능 마일리지 찾기-------------------------------------//
        Money balanceMileageMoney = Money.wons(member.getMileage().getBalance());
        Money useMileageMoney;

        if(totalPayAmount.isGreaterThanOrEqual(balanceMileageMoney)) { // 지불 값이 마일리지보다 클 경우
            useMileageMoney = balanceMileageMoney;
            totalDiscountMoney = totalDiscountMoney.plus(useMileageMoney);
        }else { // 지불 값이 마일리지보다 작을 경우
            useMileageMoney = balanceMileageMoney.minus(totalDiscountMoney);
            totalDiscountMoney = totalDiscountMoney.plus(useMileageMoney);
        }

        return DiscountContext.builder()
                .coupon(context.getCoupon())
                .pointHistories(context.getPointHistories())
                .mileage(useMileageMoney.getAmount())
                .totalDiscountAmount(totalDiscountMoney.getAmount())
                .totalPayAmount(totalAmount.minus(totalDiscountMoney).getAmount())
                .totalAmount(context.getTotalAmount())
                .memberId(context.getMemberId())
                .build();
    }

}
