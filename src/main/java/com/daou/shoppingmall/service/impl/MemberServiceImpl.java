package com.daou.shoppingmall.service.impl;

import com.daou.shoppingmall.dto.MemberDto;
import com.daou.shoppingmall.entity.*;
import com.daou.shoppingmall.repository.CouponRepository;
import com.daou.shoppingmall.repository.MemberRepository;
import com.daou.shoppingmall.repository.PointRepository;
import com.daou.shoppingmall.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    public MemberDto getMemberBy(String memberId) {
        Optional<Member> optMember = memberRepository.findById(Long.valueOf(memberId));
        if(!optMember.isPresent()) {
            throw new IllegalStateException("Not fount Member By id " + memberId);
        }
        Member member = optMember.get();
        List<Coupon> coupons = new ArrayList<>(member.getCoupons());
        List<Point> points = new ArrayList<>(member.getPoints());
        return MemberDto.builder().id(member.getId())
                .name(member.getName())
                .coupons(coupons)
                .points(points)
                .mileage(member.getMileage())
                .build();
    }
}
