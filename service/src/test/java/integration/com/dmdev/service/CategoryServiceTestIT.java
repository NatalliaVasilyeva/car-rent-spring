package integration.com.dmdev.service;

import com.dmdev.domain.dto.category.request.CategoryCreateEditRequestDto;
import com.dmdev.domain.dto.category.response.CategoryResponseDto;
import com.dmdev.domain.dto.filterdto.CategoryFilter;
import com.dmdev.service.CategoryService;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.TestDtoBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_CATEGORY_ID_FOR_DELETE;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class CategoryServiceTestIT extends IntegrationBaseTest {

    private final CategoryService categoryService;

    @Test
    void shouldSaveCategoryCorrectly() {
        var categoryCreateRequestDTO = TestDtoBuilder.createCategoryCreateEditRequestDto();
        var actualCategory = categoryService.create(categoryCreateRequestDTO);

        assertTrue(actualCategory.isPresent());
        assertEquals(categoryCreateRequestDTO.getName(), actualCategory.get().getName());
        assertEquals(categoryCreateRequestDTO.getPrice(), actualCategory.get().getPrice());
    }

    @Test
    void shouldFindAllCategories() {
        var categories = categoryService.getAll();

        assertThat(categories).hasSize(2);

        var names = categories.stream().map(CategoryResponseDto::getName).collect(toList());
        assertThat(names).containsExactlyInAnyOrder("ECONOMY", "BUSINESS");
    }

    @Test
    void shouldReturnCategoryById() {
        var categoryCreateRequestDTO = TestDtoBuilder.createCategoryCreateEditRequestDto();
        var expectedCategory = categoryService.create(categoryCreateRequestDTO);

        var actualCategory = categoryService.getById(expectedCategory.get().getId());

        assertThat(actualCategory).isNotNull();
        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void shouldReturnBrandsByPrice() {
        var categoryCreateRequestDTO = TestDtoBuilder.createCategoryCreateEditRequestDto();
        var expectedCategory = categoryService.create(categoryCreateRequestDTO).get();
        var filter = CategoryFilter.builder()
                .price(BigDecimal.valueOf(120))
                .type("equals")
                .build();

        var actualCategory = categoryService.getAllByPrice(filter);

        assertThat(actualCategory).isNotNull().hasSize(1);
        assertEquals(expectedCategory, actualCategory.get(0));
    }

    @Test
    void shouldUpdateCategoryCorrectly() {
        var categoryCreateRequestDto = TestDtoBuilder.createCategoryCreateEditRequestDto();
        var categoryUpdateRequestDto = new CategoryCreateEditRequestDto(
                "super-economy",
                BigDecimal.valueOf(120L));
        var savedCategory = categoryService.create(categoryCreateRequestDto).get();

        var actualCategory = categoryService.update(savedCategory.getId(), categoryUpdateRequestDto);

        assertThat(actualCategory).isNotNull();
        actualCategory.ifPresent(category -> assertEquals(categoryUpdateRequestDto.getName(), category.getName()));
    }

    @Test
    void shouldDeleteCategoryByIdCorrectly() {
        assertTrue(categoryService.deleteById(TEST_CATEGORY_ID_FOR_DELETE));
    }

    @Test
    void shouldNotDeleteCategoryWithNonExistsId() {

        assertFalse(categoryService.deleteById(999999L));
    }
}