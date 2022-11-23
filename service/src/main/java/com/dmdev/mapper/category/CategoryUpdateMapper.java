package com.dmdev.mapper.category;

import com.dmdev.domain.dto.category.request.CategoryCreateEditRequestDto;
import com.dmdev.domain.entity.Category;
import com.dmdev.mapper.UpdateMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryUpdateMapper implements UpdateMapper<CategoryCreateEditRequestDto, Category> {


    @Override
    public Category mapToEntity(CategoryCreateEditRequestDto requestDto, Category existing) {
        merge(requestDto, existing);
        return existing;
    }

    @Override
    public void merge(CategoryCreateEditRequestDto requestDto, Category existing) {
        existing.setName(requestDto.getName());
        existing.setPrice(requestDto.getPrice());
    }
}