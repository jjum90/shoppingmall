package com.daou.shoppingmall.service.impl;

import com.daou.shoppingmall.dto.DiscountContext;
import com.daou.shoppingmall.dto.ProductDto;
import com.daou.shoppingmall.dto.PurchaseDto;
import com.daou.shoppingmall.entity.*;
import com.daou.shoppingmall.repository.*;
import com.daou.shoppingmall.service.DiscountPolicy;
import com.daou.shoppingmall.service.OrderService;
import com.daou.shoppingmall.utils.Money;
import com.daou.shoppingmall.utils.PayType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 자동 결제 구현 서비스
 */
@Service
@RequiredArgsConstructor
public class AutoOrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final MileageRepository mileageRepository;
    private final CouponRepository couponRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderProductRepository orderProductRepository;
    private final List<DiscountPolicy> policies;

    /**
     * 자동 결제
     * 결제 우선 순위
     * 1. 할인쿠폰 - 금액대별 적용 제한 : 5%(1만원이상), 10%(2만원이상), 20%(3만원이상)
     * 2. 포인트 - 유효기간 존재(유효기간 짧은 순서대로 사용)
     * 3. 적립금
     * 4. PG 결제 - 적립금, 할인쿠폰 적용 후 부족하면 PG 연동
     * @param purchaseDto
     */
    @Override
    @Transactional
    public void paymentOf(PurchaseDto purchaseDto) {
        Optional<Member> optMember = memberRepository.findById(Long.valueOf(purchaseDto.getMemberId()));

        if(!optMember.isPresent()) {
            throw new IllegalStateException("Not found member by id " + purchaseDto.getMemberId());
        }
        Member member = optMember.get();
        DiscountContext context = discountProcessor(member, purchaseDto, getPriorityDiscountPolicy());
        Coupon coupon = saveCoupon(context);
        saveMileage(member, context);
        Order order = saveOrder(member, context, coupon);
        saveOrderProduct(purchaseDto, order);
        savePointHistory(context, order);
    }

    private Coupon saveCoupon(DiscountContext context) {
        Coupon coupon = null;

        if(!ObjectUtils.isEmpty(context.getCoupon())) {
            Optional<Coupon> optCoupon = couponRepository.findById(context.getCoupon().getId());
            if(optCoupon.isPresent()) {
                coupon = optCoupon.get();
                coupon.setUseStatus(UseStatus.USED);
            }
        }
        return coupon;
    }

    private void saveMileage(Member member, DiscountContext context) {
        if(!ObjectUtils.isEmpty(context.getMileage())) {
            Optional<Mileage> optMileage = mileageRepository.findById(member.getMileage().getId());
            if(optMileage.isPresent()) {
                Mileage mileage = optMileage.get();
                Money mileageMoney = Money.wons(mileage.getBalance()).minus(Money.wons(context.getMileage()));
                mileage.setBalance(mileageMoney.getAmount());
            }
        }
    }

    private Order saveOrder(Member member, DiscountContext context, Coupon coupon) {
        Order order = Order.save(context, coupon, member, OrderStatus.COMPLETE, PayType.AUTO);
        orderRepository.save(order);
        return order;
    }

    private void saveOrderProduct(PurchaseDto purchaseDto, Order order) {
        if(!ObjectUtils.isEmpty(purchaseDto.getOrder())){
            List<Long> productIds = purchaseDto.getOrder().getProducts().stream()
            .filter((ProductDto productDto) -> productDto.getSelectQuantity() > 0)
            .map((ProductDto::getId)).collect(Collectors.toList());

            Map<Long, ProductDto> productDtoMap = purchaseDto.getOrder().getProducts().stream()
                    .filter((ProductDto productDto) -> productDto.getSelectQuantity() > 0)
                    .collect(Collectors.toMap(ProductDto::getId, Function.identity()));

            Map<Long, Product> productsMap = productRepository.findAllById(productIds).stream()
                    .collect(Collectors.toMap(Product::getId, Function.identity()));
            List<OrderProduct> orderProducts = new ArrayList<>();

            for (int index = 0; index < productIds.size(); index++) {
                orderProducts.add(
                    OrderProduct.builder()
                            .product(productsMap.get(productIds.get(index)))
                            .orderPrice(productsMap.get(productIds.get(index)).getPrice())
                            .order(order)
                            .createdDate(LocalDateTime.now())
                            .count(productDtoMap.get(productIds.get(index)).getSelectQuantity())
                            .build()
                );
            }
            orderProductRepository.saveAll(orderProducts);
        }
    }

    private void savePointHistory(DiscountContext context, Order order) {
        if(!CollectionUtils.isEmpty(context.getPointHistories())) {
            List<PointHistory> pointHistories = context.getPointHistories();
            for (PointHistory pointHis: pointHistories) {
                pointHistoryRepository.save(
                    PointHistory.builder()
                        .point(pointHis.getPoint())
                        .order(order)
                        .createdDate(LocalDateTime.now())
                        .usePoint(pointHis.getUsePoint())
                        .build()
                );
            }
        }
    }

    @Override
    public void refundOf(String orderId) {

    }

    @Override
    public boolean isSatisfied(PurchaseDto purchaseDto) {
        return PayType.AUTO.name().equals(purchaseDto.getPayType());
    }

    @Override
    public DiscountContext processDiscount(DiscountContext context, Member member, PurchaseDto purchaseDto) {
        return context;
    }

    private DiscountPolicy[] getPriorityDiscountPolicy () {
        PriorityQueue<PriorityDiscount> priorityQueue = new PriorityQueue<>();
        for (DiscountPolicy policy : policies) {
            PayType payType = PayType.fromOf(policy);
            priorityQueue.add(new PriorityDiscount(payType.getPriority(), policy));
        }
        List<DiscountPolicy> list = priorityQueue.stream().map(priorityDiscount -> priorityDiscount.getPolicy()).collect(Collectors.toList());
        return list.toArray(new DiscountPolicy[list.size()]);
    }

    @Getter
    private class PriorityDiscount implements Comparable<PriorityDiscount>{
        private int priority;
        private DiscountPolicy policy;

        public PriorityDiscount(int priority, DiscountPolicy policy) {
            this.priority = priority;
            this.policy = policy;
        }

        @Override
        public int compareTo(PriorityDiscount target) {
            if(this.getPriority() < target.getPriority()) {
                return -1;
            }else if(this.getPriority() > target.getPriority()) {
                return 1;
            }
            return 0;
        }
    }
}
