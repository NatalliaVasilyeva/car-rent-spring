package com.dmdev.api.controller;

import com.dmdev.domain.dto.category.request.CategoryCreateEditRequestDto;
import com.dmdev.domain.dto.filterdto.CategoryFilter;
import com.dmdev.service.CategoryService;
import com.dmdev.service.exception.CategoryBadRequestException;
import com.dmdev.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class CategoryApi {

    private static final String SUCCESS_ATTRIBUTE = "success_message";

    private final CategoryService categoryService;

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String createCategory(Model model, @ModelAttribute CategoryCreateEditRequestDto category) {
        model.addAttribute("category", category);
        model.addAttribute("categories", categoryService.getAll());
        return "layout/category/create-category";
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String create(@ModelAttribute @Valid CategoryCreateEditRequestDto requestDto,
                         RedirectAttributes redirectedAttributes) {
        return categoryService.create(requestDto)
                .map(category -> {
                    redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "You create category successfully.");
                    return "redirect:/categories";
                }).orElseThrow(() -> new CategoryBadRequestException("Can not create category. Please check input parameters"));
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute @Valid CategoryCreateEditRequestDto requestDto) {
        return categoryService.update(id, requestDto)
                .map(categories -> "redirect:/categories/{id}")
                .orElseThrow(() -> new CategoryBadRequestException("Can not update category. Please check input parameters"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
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
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String findByPrice(@ModelAttribute() CategoryFilter filter, Model model) {
        var categories = categoryService.getAllByPrice(filter);
        model.addAttribute("categories", categories);

        return "layout/category/categories";
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String findAll(Model model) {
        var categories = categoryService.getAll();
        model.addAttribute("categories", categories);

        return "layout/category/categories";
    }


    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        if (!categoryService.deleteById(id)) {
            throw new NotFoundException(String.format("Category with id %s does not exist.", id));
        }
        return "redirect:/categories";
    }
}