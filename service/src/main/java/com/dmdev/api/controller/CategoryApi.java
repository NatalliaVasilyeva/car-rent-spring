package com.dmdev.api.controller;

import com.dmdev.domain.dto.category.request.CategoryCreateEditRequestDto;
import com.dmdev.domain.dto.category.response.CategoryResponseDto;
import com.dmdev.domain.dto.filterdto.CategoryFilter;
import com.dmdev.service.CategoryService;
import com.dmdev.service.exception.CategoryBadRequestException;
import com.dmdev.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class CategoryApi {

    private static final String SUCCESS_ATTRIBUTE = "success_message";

    private final CategoryService categoryService;

    @GetMapping("/create")
    public String createCategory(Model model, @ModelAttribute CategoryCreateEditRequestDto category) {
        model.addAttribute("category", category);
        model.addAttribute("categories", categoryService.getAll());
        return "layout/category/create-category";
    }

    @PostMapping()
    public String create(@ModelAttribute CategoryCreateEditRequestDto requestDto,
                         RedirectAttributes redirectedAttributes) {
        return categoryService.create(requestDto)
                .map(category -> {
                    redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "You create category successfully.");
                    return "redirect:/categories";
                }).orElseThrow(() -> new CategoryBadRequestException("Can not create category. Please check input parameters"));
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute CategoryCreateEditRequestDto requestDto) {
        return categoryService.update(id, requestDto)
                .map(categories -> "redirect:/categories/{id}")
                .orElseThrow(() -> new CategoryBadRequestException("Can not update category. Please check input parameters"));
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        return categoryService.getById(id)
                .map(category -> {
                    model.addAttribute("category", category);
                    model.addAttribute("categories", categoryService.getAll());
                    return "layout/category/category";
                })
                .orElseThrow(() -> new NotFoundException(String.format("Category with id %s does not exist.", id)));
    }

    @GetMapping("/by-price")
    public String findByPrice(@ModelAttribute() CategoryFilter filter, Model model) {
        var categories = categoryService.getAllByPrice(filter);
        model.addAttribute("categories", categories);

        return "layout/category/categories";
    }

    @GetMapping()
    public String findAll(Model model) {
        var categories = categoryService.getAll();
        model.addAttribute("categories", categories);

        return "layout/category/categories";
    }


    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!categoryService.deleteById(id)) {
            throw new NotFoundException(String.format("Category with id %s does not exist.", id));
        }
        return "redirect:/categories";
    }

}