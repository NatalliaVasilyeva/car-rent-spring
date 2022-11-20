package com.dmdev.api.rest;

import com.dmdev.domain.dto.ApiResponse;
import com.dmdev.domain.dto.filterdto.UserFilter;
import com.dmdev.domain.dto.user.request.UserChangePasswordDto;
import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.dto.user.request.UserUpdateRequestDto;
import com.dmdev.domain.dto.user.response.UserResponseDto;
import com.dmdev.service.UserService;
import com.dmdev.service.exception.NotFoundException;
import com.dmdev.service.exception.UserBadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

@Tag(name = "User API", description = "User API")
@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
public class UserRestApi {

    private final UserService userService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create user")
    public UserResponseDto create(@Parameter(required = true) @RequestBody @Valid UserCreateRequestDto userCreateRequestDto) {
        return userService.create(userCreateRequestDto)
                .orElseThrow(() -> new UserBadRequestException("Can not create user. Please check input parameters"));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update user")
    public UserResponseDto update(@Parameter(required = true) @PathVariable @Valid @NotNull Long id,
                                  @Parameter(required = true) @RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto) {
        return userService.update(id, userUpdateRequestDto)
                .orElseThrow(() -> new UserBadRequestException("Can not update user. Please check input parameters"));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a user by id")
    public UserResponseDto findById(@Parameter(required = true) @PathVariable @Valid @NotNull Long id) {
        return userService.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s does not exist.", id)));
    }

    @PostMapping("/{id}/change-password")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update user password")
    public UserResponseDto changePassword(@Parameter(required = true) @PathVariable @Valid @NotNull Long id,
                                          @Parameter(required = true) @RequestBody @Valid UserChangePasswordDto changedPasswordDto) {
        return userService.changePassword(id, changedPasswordDto)
                .orElseThrow(() -> new UserBadRequestException("Password have not been changed. Please check if old password is correct"));
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all users")
    public ApiResponse<UserResponseDto> findAll(@Parameter() @RequestBody @Valid UserFilter userFilter,
                                                @Parameter(required = true) @RequestParam(required = false, defaultValue = "1") Integer page,
                                                @Parameter() @RequestParam(required = false, defaultValue = "20") Integer size) {

        Page<UserResponseDto> usersPage = userService.getAll(userFilter, page - 1, size);

        return new ApiResponse<>(usersPage.getContent(), usersPage);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id")
    public ResponseEntity<?> delete(@Parameter(required = true) @PathVariable @Valid @NotNull Long id) {
        return userService.deleteById(id)
                ? noContent().build()
                : notFound().build();
    }
}