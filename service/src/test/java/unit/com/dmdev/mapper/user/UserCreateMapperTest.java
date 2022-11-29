package unit.com.dmdev.mapper.user;

import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.mapper.user.DriverLicenseFromUserCreateMapper;
import com.dmdev.mapper.user.UserCreateMapper;
import com.dmdev.mapper.user.UserDetailsFromUserCreateMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserCreateMapperTest {

    @Mock
    UserDetailsFromUserCreateMapper userDetailsCreateMapper;

    @Mock
    DriverLicenseFromUserCreateMapper driverLicenseCreateMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserCreateMapper userCreateMapper;

    @Test
    void shouldReturnCorrectEntity() {

        var createUserRequestDto = new UserCreateRequestDto(
                "test@gmail.com", "test", "test",
                "vasia", "pupkin", "minsk",
                "+37529111-11-11", LocalDate.now().minusYears(20),
                "ak874", LocalDate.now().minusYears(6), LocalDate.now().plusYears(4)
        );
        when(userDetailsCreateMapper.mapToEntity(createUserRequestDto)).thenReturn(UserDetails.builder().build());
        when(driverLicenseCreateMapper.mapToEntity(createUserRequestDto)).thenReturn(DriverLicense.builder().build());
        when(passwordEncoder.encode(createUserRequestDto.getPassword())).thenReturn("1111");

        var actualResult = userCreateMapper.mapToEntity(createUserRequestDto);

        assertEquals(actualResult.getEmail(), createUserRequestDto.getEmail());
        assertEquals(actualResult.getUsername(), createUserRequestDto.getUsername());
        assertEquals(actualResult.getPassword(), "1111");
    }
}