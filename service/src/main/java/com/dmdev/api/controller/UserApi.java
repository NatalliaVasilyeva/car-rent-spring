package com.dmdev.api.controller;

import com.dmdev.domain.dto.filterdto.UserFilter;
import com.dmdev.domain.dto.user.request.LoginRequestDto;
import com.dmdev.domain.dto.user.request.UserChangePasswordDto;
import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.dto.user.request.UserUpdateRequestDto;
import com.dmdev.domain.model.Role;
import com.dmdev.service.UserService;
import com.dmdev.service.exception.NotFoundException;
import com.dmdev.service.exception.UnauthorizedException;
import com.dmdev.service.exception.UserBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserApi {

    private static final String SUCCESS_ATTRIBUTE = "success_message";
    private final UserService userService;

    @PostMapping()
    public String create(@ModelAttribute("registration") UserCreateRequestDto userCreateRequestDto, BindingResult bindingResult,
                         RedirectAttributes redirectedAttributes) {

        if (bindingResult.hasErrors()) {
            redirectedAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/welcome";
        }
        return userService.create(userCreateRequestDto)
                .map(user -> {
                    redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "Your registration was successfully. Please login");
                    return "redirect:/welcome";
                }).orElseThrow(() -> new UserBadRequestException("Can not create user. Please check input parameters"));
    }

    @PostMapping("/sign-in")
    public String login(@ModelAttribute LoginRequestDto loginRequestDto,
                        RedirectAttributes redirectedAttributes,
                        HttpServletRequest request) {
        return userService.login(loginRequestDto)
                .map(user -> {
                            request.getSession().setAttribute("user", user);
                            redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "Your login was successfully. Now you can choose car");
                            return "redirect:/welcome";
                        }
                ).orElseThrow(() -> new UnauthorizedException("User with these credentials does not exist. Please try again"));
    }

    @PostMapping("/logout")
    public String logout(@ModelAttribute LoginRequestDto loginRequestDto,
                         RedirectAttributes redirectedAttributes,
                         HttpServletRequest request) {
        request.getSession().invalidate();
        redirectedAttributes.addFlashAttribute(SUCCESS_ATTRIBUTE, "You logout successfully");
        return "redirect:/welcome";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute("updateUser") UserUpdateRequestDto userUpdateRequestDto) {
        return userService.update(id, userUpdateRequestDto)
                .map(result -> "redirect:/users/{id}")
                .orElseThrow(() -> new UserBadRequestException("Can not update user. Please check input parameters"));
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        return userService.getById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    model.addAttribute("roles", Role.values());
                    return "layout/user/profile";
                })
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s does not exist.", id)));
    }

    @PostMapping("/{id}/change-password")
    public String changePassword(@PathVariable("id") Long id,
                                 @ModelAttribute UserChangePasswordDto changedPasswordDto) {
        return userService.changePassword(id, changedPasswordDto)
                .map(result -> "redirect:/users/{id}")
                .orElseThrow(() -> new UserBadRequestException("Password have not been changed. Please check if old password is correct"));
    }

    @PostMapping("/{id}/change-role")
    public String changePassword(@PathVariable("id") Long id,
                                 @PathParam(value = "role") String role) {
        return userService.changeRole(id, role)
                .map(result -> "redirect:/users")
                .orElseThrow(() -> new UserBadRequestException("Role have not been changed"));
    }

    @GetMapping()
    public String findAll(Model model,
                          @ModelAttribute @Nullable UserFilter userFilter,
                          @RequestParam(required = false, defaultValue = "1") Integer page,
                          @RequestParam(required = false, defaultValue = "20") Integer size) {

        var usersPage = userService.getAll(userFilter, page - 1, size);
        model.addAttribute("usersPage", usersPage);
        model.addAttribute("filter", userFilter);
        model.addAttribute("roles", Role.values());

        return "layout/user/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!userService.deleteById(id)) {
            throw new NotFoundException(String.format("User with id %s does not exist.", id));
        }
        return "redirect:/users";
    }
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}