package com.daou.shoppingmall.controller;

import com.daou.shoppingmall.dto.ProductDto;
import com.daou.shoppingmall.service.ProductService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.0/product")
@Slf4j
public class ProductController {
    private final ProductService productService;

    /**
     * 상품 조회 API
     * @return
     */
    @GetMapping
    public List<ProductDto> getProducts() {
        log.info("get all products");
        return productService.getProducts();
    }
}
