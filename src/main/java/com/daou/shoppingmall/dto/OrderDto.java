package com.daou.shoppingmall.dto;

import com.daou.shoppingmall.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    @NotBlank(message = "상품을 확인해주세요.")
    private String productId;
    @NotNull(message = "가격을 확인해주세요.")
    private BigDecimal amount;
    @NotNull(message = "수량을 확인해주세요.")
    private Integer quantity;
}
