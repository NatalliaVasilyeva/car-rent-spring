package unit.com.dmdev.mapper.user;

import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.mapper.user.DriverLicenseFromUserCreateMapper;
import com.dmdev.mapper.user.UserCreateMapper;
import com.dmdev.mapper.user.UserDetailsFromUserCreateMapper;
import com.dmdev.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserCreateMapperTest {

    @Mock
    UserDetailsFromUserCreateMapper userDetailsCreateMapper;

    @Mock
    DriverLicenseFromUserCreateMapper driverLicenseCreateMapper;

    @InjectMocks
    UserCreateMapper userCreateMapper;

    @Test
    void shouldReturnCorrectEntity() {

        var createUserRequestDto = new UserCreateRequestDto(
                "test@gmail.com", "test", "test",
                "vasia", "pupkin", "minsk",
                "+37529111111111", LocalDate.now().minusYears(20),
                "ak874", LocalDate.now().minusYears(6), LocalDate.now().plusYears(4)
        );
        when(userDetailsCreateMapper.map(createUserRequestDto)).thenReturn(UserDetails.builder().build());
        when(driverLicenseCreateMapper.map(createUserRequestDto)).thenReturn(DriverLicense.builder().build());

        var actualResult = userCreateMapper.map(createUserRequestDto);

        assertEquals(actualResult.getEmail(), createUserRequestDto.getEmail());
        assertEquals(actualResult.getLogin(), createUserRequestDto.getLogin());
        assertEquals(actualResult.getPassword(), SecurityUtils.securePassword(createUserRequestDto.getEmail(), createUserRequestDto.getPassword()));
    }
}