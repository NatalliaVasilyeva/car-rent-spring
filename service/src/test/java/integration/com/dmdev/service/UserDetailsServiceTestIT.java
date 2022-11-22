package integration.com.dmdev.service;

import com.dmdev.domain.dto.filterdto.UserDetailsFilter;
import com.dmdev.domain.dto.userdetails.request.UserDetailsUpdateRequestDto;
import com.dmdev.domain.dto.userdetails.response.UserDetailsResponseDto;
import com.dmdev.service.UserDetailsService;
import com.dmdev.service.UserService;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.TestDtoBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.util.List;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_USER_DETAILS_ID_FOR_DELETE;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class UserDetailsServiceTestIT extends IntegrationBaseTest {

    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @Test
    void shouldSaveUserDetailsCorrectly() {
        var userCreateRequestDTO = TestDtoBuilder.createUserCreateRequestDTO();

        var actualUser = userService.create(userCreateRequestDTO);

        assertTrue(actualUser.isPresent());
        assertEquals(userCreateRequestDTO.getName(), actualUser.get().getUserDetailsDto().getName());
        assertEquals(userCreateRequestDTO.getSurname(), actualUser.get().getUserDetailsDto().getSurname());
        assertEquals(userCreateRequestDTO.getAddress(), actualUser.get().getUserDetailsDto().getAddress());
        assertEquals(userCreateRequestDTO.getPhone(), actualUser.get().getUserDetailsDto().getPhone());
    }

    @Test
    void shouldFindAllUserDetails() {
        var userDetails = userDetailsService.getAll(UserDetailsFilter.builder().build(), 0, 4);

        assertThat(userDetails.getContent()).hasSize(2);
        assertThat(userDetails.getTotalElements()).isEqualTo(2L);
        assertThat(userDetails.getNumberOfElements()).isEqualTo(2L);

        var addresses = userDetails.getContent().stream().map(UserDetailsResponseDto::getAddress).collect(toList());
        assertThat(addresses).containsExactlyInAnyOrder("Minsk", "Minsk");
    }

    @Test
    void shouldReturnUserDetailsByFilter() {
        var userCreateRequestDto = TestDtoBuilder.createUserCreateRequestDTO();
        var userResponseDto = userService.create(userCreateRequestDto);

        var userDetailsFilter = UserDetailsFilter.builder()
                .address(userCreateRequestDto.getAddress())
                .build();

        var userDetails = userDetailsService.getAll(userDetailsFilter, 0, 4);

        assertThat(userDetails.getContent()).hasSize(3);
        assertThat(userDetails.getTotalElements()).isEqualTo(3L);
        assertThat(userDetails.getNumberOfElements()).isEqualTo(3L);
        assertThat(userDetails.getContent().get(0).getAddress()).isEqualTo(userResponseDto.get().getUserDetailsDto().getAddress());
    }

    @Test
    void shouldReturnUserDetailsByNameAndSurname() {
        var userCreateRequestDto = TestDtoBuilder.createUserCreateRequestDTO();
        var userDetailsResponseDto = userService.create(userCreateRequestDto).get().getUserDetailsDto();

        var userDetails = userDetailsService.getAllByNameAndSurname("petia", "petrov");

        assertThat(userDetails).hasSize(2);
        assertThat(userDetails.get(0).getAddress()).isEqualTo(userDetailsResponseDto.getAddress());
    }

    @Test
    void shouldReturnUserDetailsById() {
        var userCreateRequestDto = TestDtoBuilder.createUserCreateRequestDTO();
        var expectedUserDetails = userService.create(userCreateRequestDto).get().getUserDetailsDto();

        var actualUserDetails = userDetailsService.getById(expectedUserDetails.getId());

        assertThat(actualUserDetails).isNotNull();
        assertEquals(expectedUserDetails, actualUserDetails.get());
    }

    @Test
    void shouldUpdateUserDetailsCorrectly() {
        var userCreateRequestDto = TestDtoBuilder.createUserCreateRequestDTO();
        var userDetailsUpdateRequestDto = new UserDetailsUpdateRequestDto(
                "test",
                "test",
                "Moscow",
                "1111111111");

        var savedUserDetails = userService.create(userCreateRequestDto).get().getUserDetailsDto();

        var actualUserDetails = userDetailsService.update(savedUserDetails.getId(), userDetailsUpdateRequestDto);

        assertThat(actualUserDetails).isNotNull();
        actualUserDetails.ifPresent(userDetail -> {
            assertEquals(userDetailsUpdateRequestDto.getName(), userDetail.getName());
            assertEquals(userDetailsUpdateRequestDto.getSurname(), userDetail.getSurname());
            assertSame(userDetailsUpdateRequestDto.getAddress(), userDetail.getAddress());
            assertSame(userDetailsUpdateRequestDto.getPhone(), userDetail.getPhone());
        });
    }

    @Test
    void shouldDeleteUserDetailByIdCorrectly() {
        assertTrue(userDetailsService.deleteById(TEST_USER_DETAILS_ID_FOR_DELETE));
    }

    @Test
    void shouldNotDeleteUserWithNonExistsId() {
        assertFalse(userDetailsService.deleteById(999999L));
    }
}