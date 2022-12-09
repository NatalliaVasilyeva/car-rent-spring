package com.dmdev.api.controller;

import com.dmdev.domain.dto.filterdto.UserFilter;
import com.dmdev.domain.dto.user.request.UserChangePasswordDto;
import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.dto.user.request.UserUpdateRequestDto;
import com.dmdev.domain.model.Role;
import com.dmdev.service.UserService;
import com.dmdev.service.exception.NotFoundException;
import com.dmdev.service.exception.UserBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserApi {

    private static final String SUCCESS_ATTRIBUTE = "success_message";
    private final UserService userService;

    @PostMapping("/sing-up")
    public String create(@ModelAttribute @Valid UserCreateRequestDto userCreateRequestDto, BindingResult bindingResult,
                         RedirectAttributes redirectedAttributes) {
        if (bindingResult.hasErrors()) {
            redirectedAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userCreateRequestDto", bindingResult);
            redirectedAttributes.addFlashAttribute("userCreateRequestDto", userCreateRequestDto);
            System.out.println(bindingResult);
            return "redirect:/welcome";
        }
        return userService.create(userCreateRequestDto)
                .map(user -> {
                    redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "Your registration was successfully. Please login");
                    return "redirect:/welcome";
                }).orElseThrow(() -> new UserBadRequestException("Can not create user. Please check input parameters"));
    }


    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute @Valid UserUpdateRequestDto userUpdateRequestDto) {
        return userService.update(id, userUpdateRequestDto)
                .map(result -> "redirect:/users/{id}")
                .orElseThrow(() -> new UserBadRequestException("Can not update user. Please check input parameters"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String findById(@PathVariable("id") Long id, Model model) {
        return userService.getById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("roles", Role.values());
                    return "layout/user/user";
                })
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s does not exist.", id)));
    }

    @GetMapping("/profile/{id}")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String findProfileById(@PathVariable("id") Long id, Model model) {
        return userService.getById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("roles", Role.values());
                    return "layout/user/profile";
                })
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s does not exist.", id)));
    }

    @PostMapping("/{id}/change-password")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String changePassword(@PathVariable("id") Long id,
                                 @ModelAttribute @Valid UserChangePasswordDto changedPasswordDto) {

        return userService.changePassword(id, changedPasswordDto)
                .map(result -> "redirect:/users/{id}")
                .orElseThrow(() -> new UserBadRequestException("Password have not been changed. Please check if old password is correct"));
    }

    @GetMapping("/change-password")
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String changePasswordForm(Model model,
                                     @ModelAttribute @Valid UserChangePasswordDto changedPasswordDto) {
        model.addAttribute("change_password", changedPasswordDto);
        return "layout/user/change-password";
    }

    @PostMapping("/{id}/change-role")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String changeRole(@PathVariable("id") Long id,
                             @PathParam(value = "role") Role role) {
        return userService.changeRole(id, role)
                .map(result -> "redirect:/users")
                .orElseThrow(() -> new UserBadRequestException("Role have not been changed"));
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('CLIENT', 'ADMIN')")
    public String findAll(Model model,
                          @ModelAttribute UserFilter userFilter,
                          @RequestParam(required = false, defaultValue = "0") Integer page,
                          @RequestParam(required = false, defaultValue = "20") Integer size) {

        var usersPage = userService.getAll(userFilter, page, size);
        model.addAttribute("usersPage", usersPage);
        model.addAttribute("filter", userFilter);
        model.addAttribute("roles", Role.values());

        return "layout/user/users";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String delete(@PathVariable("id") Long id) {
        if (!userService.deleteById(id)) {
            throw new NotFoundException(String.format("User with id %s does not exist.", id));
        }
        return "redirect:/users";
    }
}