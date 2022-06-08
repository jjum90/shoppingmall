package com.daou.shoppingmall.service.impl;

import com.daou.shoppingmall.dto.ProductDto;
import com.daou.shoppingmall.entity.Product;
import com.daou.shoppingmall.repository.ProductRepository;
import com.daou.shoppingmall.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public List<ProductDto> getProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtoLists = new ArrayList<>();
        for (Product product : products) {
            productDtoLists.add(
                ProductDto.builder()
                        .name(product.getName())
                        .price(product.getPrice())
                        .id(product.getId())
                        .build()
            );
        }
        return productDtoLists;
    }
}
