package com.dmdev.api.controller;

import com.dmdev.domain.dto.accident.AccidentCreateRequestDto;
import com.dmdev.domain.dto.accident.AccidentResponseDto;
import com.dmdev.domain.dto.accident.AccidentUpdateRequestDto;
import com.dmdev.service.AccidentService;
import com.dmdev.service.OrderService;
import com.dmdev.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.lang.Nullable;
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
import java.util.Optional;

@Controller
@RequestMapping(path = "/accidents")
@RequiredArgsConstructor
public class AccidentApi {

    private static final String SUCCESS_ATTRIBUTE = "success_message";
    private static final String ERROR_ATTRIBUTE = "error_message";
    private final OrderService orderService;
    private final AccidentService accidentService;

    @GetMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String createOrder(@RequestParam(value = "orderId") Long orderId, Model model, @ModelAttribute AccidentCreateRequestDto accident) {
        model.addAttribute("accident", accident);
        model.addAttribute("order", orderService.getById(orderId).get());
        return "layout/accident/create-accident";
    }

    @PostMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String create(@ModelAttribute @Valid AccidentCreateRequestDto requestDto,
                         RedirectAttributes redirectedAttributes) {
        Optional<AccidentResponseDto> accident = accidentService.create(requestDto);
        if (accident.isEmpty()) {
            redirectedAttributes.addFlashAttribute(ERROR_ATTRIBUTE, "Accident can not be created");
            return "redirect:/orders";
        }

        redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "You create accident successfully.");
        return "redirect:/accidents/" + accident.get().getId();
    }


    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute @Valid AccidentUpdateRequestDto accidentUpdateRequestDto,
                         Model model,
                         RedirectAttributes redirectedAttributes) {
        Optional<AccidentResponseDto> accident = accidentService.update(id, accidentUpdateRequestDto);

        if (accident.isEmpty()) {
            redirectedAttributes.addFlashAttribute(ERROR_ATTRIBUTE, "Accident can not be updated");
        }
        model.addAttribute("orders", orderService.getAll());
        return "redirect:/accidents/{id}";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findById(@PathVariable("id") Long id, Model model) {
        return accidentService.getById(id)
                .map(accident -> {
                    model.addAttribute("accident", accident);
                    model.addAttribute("orders", orderService.getAll());
                    return "layout/accident/accident";
                })
                .orElseThrow(() -> new NotFoundException(String.format("Accident with id %s does not exist.", id)));
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAll(Model model,
                          @RequestParam(required = false, defaultValue = "0") Integer page,
                          @RequestParam(required = false, defaultValue = "20") Integer size) {
        var accidentsPage = accidentService.getAll(page, size);
        model.addAttribute("accidentsPage", accidentsPage);

        return "layout/accident/accidents";
    }

    @GetMapping("/by-order")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAllByOrderId(Model model,
                                  @RequestParam @Nullable Long orderId) {
        var accidents = accidentService.findAllByOrderId(orderId);
        var accidentsPage = new PageImpl<>(accidents);
        model.addAttribute("accidentsPage", accidentsPage);
        model.addAttribute("orders", orderService.getAll());

        return "layout/accident/accidents";
    }

    @GetMapping("/by-car")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAllByCarNumber(Model model,
                                  @RequestParam @Nullable String carNumber) {

        var accidents = accidentService.findAllByCarNumber(carNumber);
        var accidentsPage = new PageImpl<>(accidents);
        model.addAttribute("accidentsPage", accidentsPage);

        return "layout/accident/accidents";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        if (!accidentService.deleteById(id)) {
            throw new NotFoundException(String.format("Car with id %s does not exist.", id));
        }
        return "redirect:/accidents";
    }
}