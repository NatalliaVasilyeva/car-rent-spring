package com.dmdev.mapper.category;

import com.dmdev.domain.dto.category.response.CategoryResponseDto;
import com.dmdev.domain.entity.Category;
import com.dmdev.mapper.ResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryResponseMapper implements ResponseMapper<Category, CategoryResponseDto> {

    @Override
    public CategoryResponseDto mapToDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .price(category.getPrice())
                .build();
    }
}