package com.daou.shoppingmall.service;

import com.daou.shoppingmall.dto.MemberDto;
import org.springframework.data.jpa.repository.Query;

public interface MemberService {
    MemberDto getMemberBy(String memberId);
}
