package com.dmdev.api.controller;

import com.dmdev.domain.dto.brand.request.BrandCreateEditRequestDto;
import com.dmdev.domain.dto.brand.response.BrandResponseDto;
import com.dmdev.domain.projection.BrandFullView;
import com.dmdev.service.BrandService;
import com.dmdev.service.exception.BrandBadRequestException;
import com.dmdev.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping(path = "/brands")
@RequiredArgsConstructor
public class BrandApi {

    private static final String SUCCESS_ATTRIBUTE = "success_message";

    private final BrandService brandService;

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String createBrand(Model model, @ModelAttribute BrandCreateEditRequestDto brand) {
        model.addAttribute("brand", brand);
        model.addAttribute("brands", brandService.getAll());
        return "layout/brand/create-brand";
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String create(@ModelAttribute @Valid BrandCreateEditRequestDto requestDto,
                         RedirectAttributes redirectedAttributes) {
        return brandService.create(requestDto)
                .map(brand -> {
                    redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "You create brand successfully.");
                    return "redirect:/brands";
                }).orElseThrow(() -> new BrandBadRequestException("Can not create brand. Please check input parameters"));
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute @Valid BrandCreateEditRequestDto requestDto) {
        return brandService.update(id, requestDto)
                .map(driverLicense -> "redirect:/brands/{id}")
                .orElseThrow(() -> new BrandBadRequestException("Can not update brand. Please check input parameters"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String findById(@PathVariable("id") Long id, Model model) {
        return brandService.getById(id)
                .map(brand -> {
                    model.addAttribute("brand", brand);
                    model.addAttribute("brands", brandService.getAll());
                    return "layout/brand/brand";
                })
                .orElseThrow(() -> new NotFoundException(String.format("Brand with id %s does not exist.", id)));
    }

    @GetMapping("/by-name")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String findByName(@RequestParam() String name, Model model) {
        return brandService.getByName(name)
                .map(brand -> {
                    Page<BrandResponseDto> brandPage = new PageImpl<>(List.of(brand));
                    model.addAttribute("brandPage", brandPage);
                    model.addAttribute("brands", brandService.getAll());
                    return "layout/brand/brands";
                })
                .orElseThrow(() -> new NotFoundException(String.format("Brand with name %s does not exist.", name)));
    }

    @GetMapping("/by-names")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String findByNames(@RequestParam(required = false) List<String> names, Model model) {
        var brands = names != null ? brandService.getByNames(names) : new ArrayList<BrandResponseDto>();
        var brandPage = new PageImpl<>(brands);
        model.addAttribute("brandPage", brandPage);
        model.addAttribute("brands", brandService.getAll());

        return "layout/brand/brands";
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String findAll(Model model,
                          @RequestParam(required = false, defaultValue = "1") Integer page,
                          @RequestParam(required = false, defaultValue = "20") Integer size) {
        var brandPage = brandService.getAll(page - 1, size);
        model.addAttribute("brandPage", brandPage);
        model.addAttribute("brands", brandService.getAll());

        return "layout/brand/brands";
    }

    @GetMapping("/all-full-view")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAllFullView(Model model) {
        var brands = brandService.getAllFullView();
        var brandFullViewPage = new PageImpl<>(brands);
        model.addAttribute("brandFullViewPage", brandFullViewPage);
        model.addAttribute("brands", brandService.getAll());

        return "layout/brand/brands-full-view";
    }

    @GetMapping("/by-id-all-full-view")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAllFullViewById(Model model, @PathVariable("id") Long id) {
        var brand = brandService.getByIdFullView(id);
        Page<BrandFullView> brandFullViewPage = brand.isEmpty()
                ? new PageImpl<>(Collections.emptyList())
                : new PageImpl<>(List.of(brand.get()));

        model.addAttribute("brandFullViewPage", brandFullViewPage);
        model.addAttribute("brands", brandService.getAll());

        return "layout/brand/brands-full-view";
    }

    @GetMapping("/by-name-all-full-view")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAllFullViewByName(Model model,
                                        @PathVariable("id") String name) {
        var brands = brandService.getByNameFullView(name);
        var brandFullViewPage = new PageImpl<>(brands);
        model.addAttribute("brandFullViewPage", brandFullViewPage);
        model.addAttribute("brands", brandService.getAll());

        return "layout/brand/brands-full-view";
    }


    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        if (!brandService.deleteById(id)) {
            throw new NotFoundException(String.format("Brand with id %s does not exist.", id));
        }
        return "redirect:/brands";
    }
}