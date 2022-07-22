package com.daou.shoppingmall.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    @NotBlank(message = "상품명을 확인해 주세요")
    private String name;
    @NotNull(message = "상품 금액을 확인해 주세요")
    private BigDecimal price;
    private int selectQuantity;
}
