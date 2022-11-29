package com.dmdev.api.controller;

import com.dmdev.domain.dto.car.CarCreateRequestDto;
import com.dmdev.domain.dto.car.CarUpdateRequestDto;
import com.dmdev.domain.dto.filterdto.CarFilter;
import com.dmdev.domain.model.Color;
import com.dmdev.service.BrandService;
import com.dmdev.service.CarService;
import com.dmdev.service.CategoryService;
import com.dmdev.service.ModelService;
import com.dmdev.service.exception.CarBadRequestException;
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
@RequestMapping(path = "/cars")
@RequiredArgsConstructor
public class CarApi {

    private static final String SUCCESS_ATTRIBUTE = "success_message";

    private final CarService carService;
    private final BrandService brandService;
    private final ModelService modelService;
    private final CategoryService categoryService;

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String createCar(Model model, @ModelAttribute CarCreateRequestDto car) {
        model.addAttribute("car", car);
        model.addAttribute("brands", brandService.getAll());
        model.addAttribute("models", modelService.getAll());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("colors", Color.values());
        return "layout/car/create-car";
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String create(@ModelAttribute CarCreateRequestDto requestDto,
                         RedirectAttributes redirectedAttributes) {
        return carService.create(requestDto)
                .map(car -> {
                    redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "You create car successfully.");
                    return "redirect:/cars";
                }).orElseThrow(() -> new CarBadRequestException("Can not create car. Please check input parameters"));
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute CarUpdateRequestDto requestDto) {
        return carService.update(id, requestDto)
                .map(car -> "redirect:/cars/{id}")
                .orElseThrow(() -> new CarBadRequestException("Can not update car. Please check input parameters"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String findById(@PathVariable("id") Long id, Model model) {
        return carService.getById(id)
                .map(car -> {
                    model.addAttribute("car", car);
                    model.addAttribute("models", modelService.getAll());
                    model.addAttribute("categories", categoryService.getAll());
                    model.addAttribute("colors", Color.values());
                    return "layout/car/car";
                })
                .orElseThrow(() -> new NotFoundException(String.format("Car with id %s does not exist.", id)));
    }

    @GetMapping("/by-number")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String findByNumber(@RequestParam("number") String number, Model model) {
        return carService.getByCarNumber(number)
                .map(car -> {
                    model.addAttribute("car", car);
                    return "layout/car/car";
                })
                .orElseThrow(() -> new NotFoundException(String.format("Car with number %s does not exist.", number)));
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public String findAll(Model model,
                          @ModelAttribute CarFilter carFilter,
                          @RequestParam(required = false, defaultValue = "1") Integer page,
                          @RequestParam(required = false, defaultValue = "20") Integer size) {
        var carPage = carService.getAll(carFilter, page - 1, size);
        model.addAttribute("carPage", carPage);
        model.addAttribute("filter", carFilter);
        model.addAttribute("modelNames", modelService.getAll());
        model.addAttribute("brandNames", brandService.getAll());
        model.addAttribute("categoryNames", categoryService.getAll());

        return "layout/car/cars";
    }

    @GetMapping("/with-accidents")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAllWithAccidents(Model model,
                                       @ModelAttribute CarFilter carFilter) {
        var cars = carService.getAllWithAccidents();
        var carPage = new PageImpl<>(cars);
        model.addAttribute("carPage", carPage);
        model.addAttribute("filter", carFilter);
        model.addAttribute("modelNames", modelService.getAll());
        model.addAttribute("brandNames", brandService.getAll());
        model.addAttribute("categoryNames", categoryService.getAll());

        return "layout/car/cars";
    }

    @GetMapping("/without-accidents")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAllWithoutAccidents(Model model,
                                          @ModelAttribute CarFilter carFilter) {
        var cars = carService.getAllWithoutAccidents();
        var carPage = new PageImpl<>(cars);
        model.addAttribute("carPage", carPage);
        model.addAttribute("filter", carFilter);
        model.addAttribute("modelNames", modelService.getAll());
        model.addAttribute("brandNames", brandService.getAll());
        model.addAttribute("categoryNames", categoryService.getAll());

        return "layout/car/cars";
    }

    @GetMapping("/under-repair")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAllUnderRepair(Model model,
                                     @ModelAttribute CarFilter carFilter) {
        var cars = carService.getAllAllUnderRepair();
        var carPage = new PageImpl<>(cars);
        model.addAttribute("carPage", carPage);
        model.addAttribute("filter", carFilter);
        model.addAttribute("modelNames", modelService.getAll());
        model.addAttribute("brandNames", brandService.getAll());
        model.addAttribute("categoryNames", categoryService.getAll());

        return "layout/car/cars";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        if (!carService.deleteById(id)) {
            throw new NotFoundException(String.format("Car with id %s does not exist.", id));
        }
        return "redirect:/cars";
    }
}