package com.daou.shoppingmall.repository;

import com.daou.shoppingmall.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{

	/**
	 * 멤버 정보 조회
	 * @param id
	 * @return
	 */
	Optional<Member> findById(Long id);
}
