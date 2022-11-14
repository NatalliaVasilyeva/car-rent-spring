package unit.com.dmdev.service;

import com.dmdev.domain.dto.filterdto.UserFilter;
import com.dmdev.domain.dto.user.request.UserUpdateRequestDto;
import com.dmdev.domain.dto.user.response.UserResponseDto;
import com.dmdev.domain.model.Role;
import com.dmdev.service.UserService;
import com.dmdev.service.exception.UserBadRequestException;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.TestDtoBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_USER_ID;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({MockitoExtension.class})
class UserServiceTest extends IntegrationBaseTest {

    @Autowired
    private UserService userService;




    @Test
    void shouldSaveUserCorrectly() {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();

        var actualUser = userService.createUser(userCreateRequestDTO);

        assertTrue(actualUser.isPresent());
        assertEquals(userCreateRequestDTO.getName(), actualUser.get().getUserDetailsDto().getName());
        assertEquals(userCreateRequestDTO.getSurname(), actualUser.get().getUserDetailsDto().getSurname());
        assertEquals(userCreateRequestDTO.getEmail(), actualUser.get().getEmail());
        assertEquals(userCreateRequestDTO.getLogin(), actualUser.get().getLogin());
        assertEquals(userCreateRequestDTO.getDriverLicenseNumber(), actualUser.get().getDriverLicenseDto().getDriverLicenseNumber());
        assertSame(Role.CLIENT, actualUser.get().getRole());
    }

    @Test
    void shouldThrowExceptionWhenSaveUserWithExistsEmail() {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTOWithExistsEmail();

        var result = assertThrowsExactly(UserBadRequestException.class, () -> userService.createUser(userCreateRequestDTO));

        assertEquals( "400 BAD_REQUEST \"User with email 'admin@gmail.com' already exists\"", result.getMessage());
    }


    @Test
    void shouldFindAllUsers() {
        Page<UserResponseDto> users = userService.getUsers(0, 4);

        assertThat(users.getContent()).hasSize(2);
        assertThat(users.getTotalElements()).isEqualTo(2L);
        assertThat(users.getNumberOfElements()).isEqualTo(2L);

        List<String> emails = users.getContent().stream().map(UserResponseDto::getEmail).collect(toList());
        assertThat(emails).containsExactlyInAnyOrder("admin@gmail.com", "client@gmail.com");
    }

    @Test
    void shouldReturnUsersByFilter() {
        var userCreateRequestDto = TestDtoBuilder.createUserCreateRequestDTO();
        var userResponseDto = userService.createUser(userCreateRequestDto);

        var userFilter = UserFilter.builder()
                .email(userCreateRequestDto.getEmail())
                .build();

        Page<UserResponseDto> users = userService.getUsersByFilter(userFilter, 0, 4);

        assertThat(users.getContent()).hasSize(1);
        assertThat(users.getTotalElements()).isEqualTo(1L);
        assertThat(users.getNumberOfElements()).isEqualTo(1L);

        assertThat(users.getContent().get(0).getEmail()).isEqualTo(userResponseDto.get().getEmail());
        assertThat(users.getContent().get(0).getLogin()).isEqualTo(userResponseDto.get().getLogin());
        assertThat(users.getContent().get(0).getUserDetailsDto().getAddress()).isEqualTo(userResponseDto.get().getUserDetailsDto().getAddress());
    }

    @Test
    void shouldReturnUserById() {
        var userCreateRequestDto = TestDtoBuilder.createUserCreateRequestDTO();
        var expectedUser = userService.createUser(userCreateRequestDto);

        var actualUser = userService.getUser(expectedUser.get().getId());

        assertThat(actualUser).isNotNull();
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void shouldUpdateUserCorrectly() {
        var userCreateRequestDto = TestDtoBuilder.createUserCreateRequestDTO();
        var userUpdateRequestDto = new UserUpdateRequestDto(
                "test1@gmal.com",
                "test",
                Role.CLIENT);
        var savedUser = userService.createUser(userCreateRequestDto);

        var actualUser = userService.updateUser(savedUser.get().getId(), userUpdateRequestDto);

        assertThat(actualUser).isNotNull();
        actualUser.ifPresent(user -> {
            assertEquals(userUpdateRequestDto.getEmail(), user.getEmail());
            assertEquals(userUpdateRequestDto.getLogin(), user.getLogin());
            assertSame(userUpdateRequestDto.getRole(), user.getRole());
        });
    }

    @Test
    void shouldDeleteUserByIdCorrectly() {
        assertTrue(userService.deleteUserById(TEST_EXISTS_USER_ID));
    }

    @Test
    void shouldNotDeleteUserWithNonExistsId() {
        assertFalse(userService.deleteUserById(999999L));
    }
}