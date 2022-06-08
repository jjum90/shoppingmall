package com.daou.shoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseDto {
    @NotBlank(message = "멤버를 확인해주세요.")
    private String memberId;
    @NotBlank(message = "결제 타입을 확인해주세요.")
    private String payType;
    private List<String> pointIds;
    private List<String> couponIds;
//    private BigDecimal mileage;
    @NotNull
    private BigDecimal totalAmount;
    @NotNull(message = "주문 리스트를 확인해 주세요")
    @Valid
    List<OrderDto> orders = new ArrayList<>();
}
