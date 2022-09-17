package com.daou.shoppingmall.service.impl;

import com.daou.shoppingmall.dto.MemberDto;
import com.daou.shoppingmall.entity.Coupon;
import com.daou.shoppingmall.entity.Member;
import com.daou.shoppingmall.entity.Point;
import com.daou.shoppingmall.entity.UseStatus;
import com.daou.shoppingmall.repository.MemberRepository;
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
            throw new IllegalStateException("Not found member by id " + memberId);
        }
        // TODO fetch join Query 로 n + 1 문제 해결하기
        Member member = optMember.get();
        List<Coupon> coupons = new ArrayList<>(member.getCoupons());
        coupons = coupons.stream().filter((Coupon coupon)->
            coupon.getUseStatus().name().equals(UseStatus.UNUSED.name())
        ).collect(Collectors.toList());

        List<Point> points = new ArrayList<>(member.getPoints());

        return MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .coupons(coupons)
                .points(points)
                .mileage(member.getMileage())
                .build();
    }
}
