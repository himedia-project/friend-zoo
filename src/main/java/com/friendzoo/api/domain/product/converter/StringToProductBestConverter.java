package com.friendzoo.api.domain.product.converter;

import com.friendzoo.api.domain.product.enums.ProductBest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToProductBestConverter implements Converter<String, ProductBest> {
    @Override
    public ProductBest convert(String source) {
        return ProductBest.valueOf(source.toUpperCase()); // 대문자로 변환하여 매칭
    }
}