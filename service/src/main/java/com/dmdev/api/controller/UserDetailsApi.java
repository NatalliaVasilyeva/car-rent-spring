package com.dmdev.api.controller;

import com.dmdev.domain.dto.filterdto.UserDetailsFilter;
import com.dmdev.domain.dto.userdetails.request.UserDetailsCreateRequestDto;
import com.dmdev.domain.dto.userdetails.request.UserDetailsUpdateRequestDto;
import com.dmdev.service.UserDetailsService;
import com.dmdev.service.exception.NotFoundException;
import com.dmdev.service.exception.UserDetailsBadRequestException;
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

import java.time.LocalDate;

@Controller
@RequestMapping(path = "/user-details")
@RequiredArgsConstructor
public class UserDetailsApi {

    private static final String SUCCESS_ATTRIBUTE = "success_message";

    private final UserDetailsService userDetailsService;

    @PostMapping()
    public String create(@ModelAttribute UserDetailsCreateRequestDto requestDto,
                         RedirectAttributes redirectedAttributes) {
        return userDetailsService.create(requestDto)
                .map(userDetails -> {
                    redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "You create details successfully.");
                    return "redirect:/users/" + userDetails.getUserId();
                }).orElseThrow(() -> new UserDetailsBadRequestException("Can not create user details. Please check input parameters"));
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute UserDetailsUpdateRequestDto requestDto) {
        return userDetailsService.update(id, requestDto)
                .map(userDetails -> "redirect:/users/" + userDetails.getUserId())
                .orElseThrow(() -> new UserDetailsBadRequestException("Can not update user details. Please check input parameters"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findById(@PathVariable("id") Long id, Model model) {
        return userDetailsService.getById(id)
                .map(userDetails -> {
                    model.addAttribute("userDetails", userDetails);
                    return "layout/user/user-detail";
                })
                .orElseThrow(() -> new NotFoundException(String.format("User details with id %s does not exist.", id)));
    }

    @GetMapping("/by-user-id")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String findByUserId(@RequestParam() Long id, Model model) {
        return userDetailsService.getByUserId(id)
                .map(userDetails -> {
                    model.addAttribute("userDetails", userDetails);
                    return "layout/user/user-detail";
                })
                .orElseThrow(() -> new NotFoundException(String.format("User details with user id %s does not exist.", id)));
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAll(Model model,
                          @ModelAttribute UserDetailsFilter userDetailsFilter,
                          @RequestParam(required = false, defaultValue = "1") Integer page,
                          @RequestParam(required = false, defaultValue = "20") Integer size) {
        var usersDetailsPage = userDetailsService.getAll(userDetailsFilter, page - 1, size);
        model.addAttribute("filter", userDetailsFilter);
        model.addAttribute("usersDetailsPage", usersDetailsPage);

        return "layout/user/user-details";
    }

    @GetMapping("/by-name-surname")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAllByUserNameAndSurname(Model model,
                                              @ModelAttribute UserDetailsFilter userDetailsFilter,
                                              @RequestParam(required = false) String name,
                                              @RequestParam(required = false) String surname) {
        var usersDetails = userDetailsService.getAllByNameAndSurname(name, surname);
        var usersDetailsPage = new PageImpl<>(usersDetails);
        model.addAttribute("filter", userDetailsFilter);
        model.addAttribute("usersDetailsPage", usersDetailsPage);

        return "layout/user/user-details";
    }

    @GetMapping("/by-registration-date")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAllByRegistrationDate(Model model,
                                            @ModelAttribute UserDetailsFilter userDetailsFilter,
                                            @RequestParam(required = false) LocalDate registrationDate) {
        var usersDetails = userDetailsService.getAllByRegistrationDate(registrationDate);
        var usersDetailsPage = new PageImpl<>(usersDetails);
        model.addAttribute("filter", userDetailsFilter);
        model.addAttribute("usersDetailsPage", usersDetailsPage);

        return "layout/user/user-details";
    }

    @GetMapping("/by-registration-dates")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findAllByRegistrationDates(Model model,
                                             @ModelAttribute UserDetailsFilter userDetailsFilter,
                                             @RequestParam(required = false) LocalDate from,
                                             @RequestParam(required = false) LocalDate to) {
        var usersDetails = userDetailsService.getAllByRegistrationDates(from, to);
        var usersDetailsPage = new PageImpl<>(usersDetails);
        model.addAttribute("filter", userDetailsFilter);
        model.addAttribute("usersDetailsPage", usersDetailsPage);

        return "layout/user/user-details";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        if (!userDetailsService.deleteById(id)) {
            throw new NotFoundException(String.format("User details with id %s does not exist.", id));
        }
        return "redirect:/user-details";
    }
}