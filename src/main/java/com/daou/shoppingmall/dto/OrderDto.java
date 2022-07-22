package com.daou.shoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    @NotNull(message = "주문 금액을 확인해 주세요")
    private BigDecimal amount;
    @Valid
    @NotNull(message = "주문 상품을 확인해 주세요")
    private List<ProductDto> products;
}
