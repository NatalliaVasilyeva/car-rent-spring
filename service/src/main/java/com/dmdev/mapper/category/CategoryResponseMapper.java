package com.dmdev.mapper.category;

import com.dmdev.domain.dto.category.response.CategoryResponseDto;
import com.dmdev.domain.entity.Category;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryResponseMapper implements Mapper<Category, CategoryResponseDto> {

    @Override
    public CategoryResponseDto map(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .price(category.getPrice())
                .build();
    }

    public CategoryResponseDto mapNames(Category category) {
        return CategoryResponseDto.builder()
                .name(category.getName())
                .build();
    }
}