package com.dmdev.api.controller;

import com.dmdev.domain.dto.driverlicense.request.DriverLicenseCreateRequestDto;
import com.dmdev.domain.dto.driverlicense.request.DriverLicenseUpdateRequestDto;
import com.dmdev.service.DriverLicenseService;
import com.dmdev.service.exception.DriverLicenseBadRequestException;
import com.dmdev.service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(path = "/driver-licenses")
@RequiredArgsConstructor
public class DriverLicenseApi {

    private static final String SUCCESS_ATTRIBUTE = "success_message";

    private final DriverLicenseService driverLicenseService;

    @PostMapping()
    public String create(@ModelAttribute DriverLicenseCreateRequestDto requestDto,
                         RedirectAttributes redirectedAttributes) {
        return driverLicenseService.create(requestDto)
                .map(driverLicense -> {
                    redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "You create driver license successfully.");
                    return "redirect:/users/" + driverLicense.getUserId();
                }).orElseThrow(() -> new DriverLicenseBadRequestException("Can not create driver license. Please check input parameters"));
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute DriverLicenseUpdateRequestDto requestDto) {
        return driverLicenseService.update(id, requestDto)
                .map(driverLicense -> "redirect:/users/" + driverLicense.getUserId())
                .orElseThrow(() -> new DriverLicenseBadRequestException("Can not update user details. Please check input parameters"));
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        return driverLicenseService.getById(id)
                .map(driverLicense -> {
                    model.addAttribute("driverLicense", driverLicense);
                    return "layout/user/driver-license";
                })
                .orElseThrow(() -> new NotFoundException(String.format("Driver license with id %s does not exist.", id)));
    }

    @GetMapping("/by-user-id")
    public String findByUserId(@RequestParam() Long id, Model model) {
        return driverLicenseService.getByUserId(id)
                .map(driverLicense -> {
                    model.addAttribute("driverLicense", driverLicense);
                    return "layout/user/driver-license";
                })
                .orElseThrow(() -> new NotFoundException(String.format("Driver license for user id %s does not exist.", id)));
    }

    @GetMapping()
    public String findAll(Model model,
                          @RequestParam(required = false, defaultValue = "1") Integer page,
                          @RequestParam(required = false, defaultValue = "20") Integer size) {

        var driverLicensesPage = driverLicenseService.getAll(page - 1, size);
        model.addAttribute("driverLicensesPage", driverLicensesPage);

        return "layout/user/driver-licenses";
    }

    @GetMapping("/by-number")
    public String findByNumber(Model model, @RequestParam(required = false) String number) {
        var driverLicenses = driverLicenseService.getByNumber(number);
        var driverLicensesPage = new PageImpl<>(driverLicenses);
        model.addAttribute("driverLicensesPage", driverLicensesPage);

        return "layout/user/driver-licenses";
    }

    @GetMapping("/expired")
    public String findAllExpiredLicenses(Model model) {
        var driverLicenses = driverLicenseService.getAllExpiredDriverLicenses();
        var driverLicensesPage = new PageImpl<>(driverLicenses);
        model.addAttribute("driverLicensesPage", driverLicensesPage);

        return "layout/user/driver-licenses";
    }


    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!driverLicenseService.deleteById(id)) {
            throw new NotFoundException(String.format("Driver license with id %s does not exist.", id));
        }
        return "redirect:/driver-licenses";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}