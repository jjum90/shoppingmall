package com.daou.shoppingmall.controller;

import com.daou.shoppingmall.dto.MemberDto;
import com.daou.shoppingmall.service.MemberService;
import com.daou.shoppingmall.service.impl.MemberServiceImpl;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1.0/member")
@RequiredArgsConstructor
@RestController
@Slf4j
public class MemberController {

    private final MemberService memberService;

    /**
     * 멤버 조회 API
     * @param memberId
     * @return
     * @throws NotFoundException
     */
    @GetMapping("/{memberId}")
    public MemberDto getMemberBy(@PathVariable String memberId) throws NotFoundException {
        log.info("get member info by {} ", memberId);
       return memberService.getMemberBy(memberId);
    }
}
