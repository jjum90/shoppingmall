package com.daou.shoppingmall.dto;

import com.daou.shoppingmall.entity.Coupon;
import com.daou.shoppingmall.entity.Mileage;
import com.daou.shoppingmall.entity.Order;
import com.daou.shoppingmall.entity.Point;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    @NotBlank(message = "멤버 아이디를 확인해주세요.")
    private Long id;
    @NotBlank(message = "멤버 이름을 확인해주세요.")
    private String name;

    private List<Coupon> coupons = new ArrayList<>();

    private List<Order> order = new ArrayList<>();

    private List<Point> points = new ArrayList<>();

    private Mileage mileage;
}
