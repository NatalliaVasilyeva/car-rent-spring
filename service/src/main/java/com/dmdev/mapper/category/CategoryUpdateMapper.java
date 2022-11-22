package com.dmdev.mapper.category;

import com.dmdev.domain.dto.category.request.CategoryCreateEditRequestDto;
import com.dmdev.domain.entity.Category;
import com.dmdev.mapper.Mapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryUpdateMapper implements Mapper<CategoryCreateEditRequestDto, Category> {

    @Override
    public Category map(CategoryCreateEditRequestDto object) {
        return Category.builder()
                .name(object.getName())
                .build();
    }

    @Override
    public Category map(CategoryCreateEditRequestDto requestDto, Category existing) {
        merge(requestDto, existing);
        return existing;
    }

    private void merge(CategoryCreateEditRequestDto requestDto, Category existing) {
        existing.setName(requestDto.getName());
        existing.setPrice(requestDto.getPrice());
    }
}