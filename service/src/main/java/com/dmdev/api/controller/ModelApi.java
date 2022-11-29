package com.dmdev.api.controller;

import com.dmdev.domain.dto.filterdto.ModelFilter;
import com.dmdev.domain.dto.model.ModelCreateRequestDto;
import com.dmdev.domain.dto.model.ModelUpdateRequestDto;
import com.dmdev.domain.model.EngineType;
import com.dmdev.domain.model.Transmission;
import com.dmdev.service.BrandService;
import com.dmdev.service.ModelService;
import com.dmdev.service.exception.ModelBadRequestException;
import com.dmdev.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
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

@Controller
@RequestMapping(path = "/models")
@RequiredArgsConstructor
public class ModelApi {

    private static final String SUCCESS_ATTRIBUTE = "success_message";

    private final ModelService modelService;
    private final BrandService brandService;

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String createModel(Model model, @ModelAttribute ModelCreateRequestDto carModel) {
        model.addAttribute("model", carModel);
        model.addAttribute("brands", brandService.getAll());
        model.addAttribute("transmission", Transmission.values());
        model.addAttribute("engineType", EngineType.values());
        return "layout/model/create-model";
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String create(@ModelAttribute ModelCreateRequestDto requestDto,
                         RedirectAttributes redirectedAttributes) {
        return modelService.create(requestDto)
                .map(car -> {
                    redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "You create model successfully.");
                    return "redirect:/models";
                }).orElseThrow(() -> new ModelBadRequestException("Can not create model. Please check input parameters"));
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute ModelUpdateRequestDto requestDto) {
        return modelService.update(id, requestDto)
                .map(driverLicense -> "redirect:/models/{id}")
                .orElseThrow(() -> new ModelBadRequestException("Can not update model. Please check input parameters"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String findById(@PathVariable("id") Long id, Model model) {
        return modelService.getById(id)
                .map(carModel -> {
                    model.addAttribute("model", carModel);
                    model.addAttribute("transmissions", Transmission.values());
                    model.addAttribute("engineTypes", EngineType.values());
                    return "layout/model/model";
                })
                .orElseThrow(() -> new NotFoundException(String.format("Model with id %s does not exist.", id)));
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String findAll(Model model,
                          @ModelAttribute ModelFilter modelFilter,
                          @RequestParam(required = false, defaultValue = "1") Integer page,
                          @RequestParam(required = false, defaultValue = "20") Integer size) {
        var modelPage = modelService.getAll(modelFilter, page - 1, size);
        model.addAttribute("modelPage", modelPage);
        model.addAttribute("models", modelService.getAll());
        model.addAttribute("brands", brandService.getAll());
        model.addAttribute("transmissions", Transmission.values());
        model.addAttribute("engineType", EngineType.values());
        model.addAttribute("filter", modelFilter);

        return "layout/model/models";
    }

    @GetMapping("/brand-id")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAllByBrandId(Model model,
                                   @RequestParam("brandId") Long brandId,
                                   @ModelAttribute ModelFilter modelFilter) {
        var models = modelService.getAllByBrandId(brandId);
        var modelPage = new PageImpl<>(models);
        model.addAttribute("modelPage", modelPage);
        model.addAttribute("models", modelService.getAll());
        model.addAttribute("brands", brandService.getAll());
        model.addAttribute("transmissions", Transmission.values());
        model.addAttribute("engineType", EngineType.values());
        model.addAttribute("filter", modelFilter);

        return "layout/model/models";
    }


    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        if (!modelService.deleteById(id)) {
            throw new NotFoundException(String.format("Model with id %s does not exist.", id));
        }
        return "redirect:/models";
    }
}