package com.dmdev.api;

import com.dmdev.domain.dto.filterdto.UserFilter;
import com.dmdev.domain.dto.user.request.LoginRequestDto;
import com.dmdev.domain.dto.user.request.UserChangePasswordDto;
import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.dto.user.request.UserUpdateRequestDto;
import com.dmdev.domain.dto.user.response.UserResponseDto;
import com.dmdev.domain.model.Role;
import com.dmdev.service.UserService;
import com.dmdev.service.exception.NotFoundException;
import com.dmdev.service.exception.UnauthorizedException;
import com.dmdev.service.exception.UserBadRequestException;
import com.dmdev.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

import static com.dmdev.api.UserApi.USER_ENDPOINT;

@Controller
@RequestMapping(path = USER_ENDPOINT)
@RequiredArgsConstructor
public class UserApi {

    static final String USER_ENDPOINT = "/api/v1/users";

    @Autowired
    private UserService userService;


    @PostMapping()
    public String create(@ModelAttribute("registration") UserCreateRequestDto userCreateRequestDto,
                         RedirectAttributes redirectedAttributes) {
        return userService.createUser(userCreateRequestDto)
                .map(user -> {
                    redirectedAttributes.addFlashAttribute("success_message", "Your registration was successfully. Please login");
                    return "redirect:/api/v1/welcome";
                }).orElseThrow(() -> new UserBadRequestException("Can not create user. Please check input parameters"));
    }

    @PostMapping("/sign-in")
    public String login(@ModelAttribute LoginRequestDto loginRequestDto,
                        RedirectAttributes redirectedAttributes,
                        HttpServletRequest request) {
        return userService.login(loginRequestDto)
                .map(user -> {
                            request.getSession().setAttribute("user", user);
                            redirectedAttributes.addFlashAttribute("success_message", "Your login was successfully. Now you can choose car");
                            return "redirect:/api/v1/welcome";
                        }
                ).orElseThrow(() -> new UnauthorizedException("User with these credentials does not exist. Please try again"));
    }

    @PostMapping("/logout")
    public String logout(@ModelAttribute LoginRequestDto loginRequestDto,
                         RedirectAttributes redirectedAttributes,
                         HttpServletRequest request) {
        HttpSession session = request.getSession();
        Enumeration<String> attrNames = request.getSession().getAttributeNames();
        while (attrNames.hasMoreElements()) {
            session.removeAttribute(attrNames.nextElement());
        }
        redirectedAttributes.addFlashAttribute("success_message", "You logout successfully");
        return "redirect:/api/v1/welcome";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute("updateUser") UserUpdateRequestDto userUpdateRequestDto) {
        return userService.updateUser(id, userUpdateRequestDto)
                .map(result -> "redirect:/api/v1/users/{id}")
                .orElseThrow(() -> new UserBadRequestException("Can not update user. Please check input parameters"));
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        return userService.getUser(id)
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
                .map(result -> "redirect:/api/v1/users/{id}")
                .orElseThrow(() -> new UserBadRequestException("Password have not been changed. Please check if old password is correct"));
    }


    @GetMapping()
    public String findAll(Model model,
                          @ModelAttribute @Nullable UserFilter userFilter,
                          @RequestParam(required = false, defaultValue = "1") Integer page,
                          @RequestParam(required = false, defaultValue = "20") Integer size) {
        Page<UserResponseDto> usersPage;
        if (!AppUtils.checkNull(userFilter)) {
            usersPage = userService.getUsersByFilter(userFilter, page - 1, size);
        } else {
            usersPage = userService.getUsers(page - 1, size);
        }
        model.addAttribute("usersPage", usersPage);

        return "layout/user/users";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        if (!userService.deleteUserById(id)) {
            throw new NotFoundException(String.format("User with id %s does not exist.", id));
        }
        return "redirect:/api/v1/users";
    }
}