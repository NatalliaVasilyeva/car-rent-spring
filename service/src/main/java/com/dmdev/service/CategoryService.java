package com.dmdev.service;

import com.dmdev.domain.dto.category.request.CategoryCreateEditRequestDto;
import com.dmdev.domain.dto.category.response.CategoryResponseDto;
import com.dmdev.domain.dto.filterdto.CategoryFilter;
import com.dmdev.domain.entity.Category;
import com.dmdev.mapper.category.CategoryCreateEditMapper;
import com.dmdev.mapper.category.CategoryResponseMapper;
import com.dmdev.mapper.category.CategoryUpdateMapper;
import com.dmdev.repository.CategoryRepository;
import com.dmdev.service.exception.BrandBadRequestException;
import com.dmdev.service.exception.CategoryBadRequestException;
import com.dmdev.service.exception.ExceptionMessageUtil;
import com.dmdev.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryCreateEditMapper categoryCreateEditMapper;
    private final CategoryUpdateMapper categoryUpdateMapper;
    private final CategoryResponseMapper categoryResponseMapper;

    @Transactional
    public Optional<CategoryResponseDto> create(CategoryCreateEditRequestDto categoryCreateEditRequestDto) {
        checkCategoryNameIsUnique(categoryCreateEditRequestDto.getName());

        return Optional.of(categoryCreateEditMapper.map(categoryCreateEditRequestDto))
                .map(categoryRepository::save)
                .map(categoryResponseMapper::map);

    }

    @Transactional
    public Optional<CategoryResponseDto> update(Long id, CategoryCreateEditRequestDto categoryCreateEditRequestDto) {
        var existingCategory = getByIdOrElseThrow(id);

        if (!existingCategory.getName().equals(categoryCreateEditRequestDto.getName())) {
            checkCategoryNameIsUnique(categoryCreateEditRequestDto.getName());
        }

        return Optional.of(categoryUpdateMapper.map(categoryCreateEditRequestDto, existingCategory))
                .map(categoryRepository::save)
                .map(categoryResponseMapper::map);
    }

    @Transactional(readOnly = true)
    public Optional<CategoryResponseDto> getById(Long id) {
        return Optional.of(getByIdOrElseThrow(id))
                .map(categoryResponseMapper::map);
    }
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAll() {
        return categoryRepository.findAll().stream()
                .map(categoryResponseMapper::map)
                .collect(toList());
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllByPrice(CategoryFilter filter) {
        var price = filter.getPrice();
        var type = filter.getType();
        List<CategoryResponseDto> categories;

        switch (type) {
            case "equals":
                categories = this.getAllByPriceEquals(price);
                break;
            case "greater":
                categories = this.getAllByPriceGreater(price);
                break;
            default:
                categories = this.getAllByPriceLess(price);
                break;
        }
        return categories;
    }

    private List<CategoryResponseDto> getAllByPriceEquals(BigDecimal price) {
        return categoryRepository.findAllByPrice(price).stream()
                .map(categoryResponseMapper::map)
                .collect(toList());
    }

    private List<CategoryResponseDto> getAllByPriceLess(BigDecimal price) {
        return categoryRepository.findAllByPriceLessThanEqual(price).stream()
                .map(categoryResponseMapper::map)
                .collect(toList());
    }

    private List<CategoryResponseDto> getAllByPriceGreater(BigDecimal price) {
        return categoryRepository.findAllByPriceGreaterThanEqual(price).stream()
                .map(categoryResponseMapper::map)
                .collect(toList());
    }

    private Category getByIdOrElseThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionMessageUtil.getNotFoundMessage("Category",  "id", id)));
    }

    private void checkCategoryNameIsUnique(String categoryName) {
        if (categoryRepository.existsByNameIgnoringCase(categoryName)) {
            throw new CategoryBadRequestException(String.format(ExceptionMessageUtil.getAlreadyExistsMessage("Category",  "name", categoryName)));
        }
    }
}