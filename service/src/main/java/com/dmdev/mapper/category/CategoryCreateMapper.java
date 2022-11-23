package com.dmdev.mapper.category;

import com.dmdev.domain.dto.category.request.CategoryCreateEditRequestDto;
import com.dmdev.domain.entity.Category;
import com.dmdev.mapper.CreateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryCreateMapper implements CreateMapper<CategoryCreateEditRequestDto, Category> {

    @Override
    public Category mapToEntity(CategoryCreateEditRequestDto requestDto) {
        return Category.builder()
                .name(requestDto.getName())
                .price(requestDto.getPrice())
                .build();
    }
}