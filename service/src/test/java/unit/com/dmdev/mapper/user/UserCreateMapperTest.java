package unit.com.dmdev.mapper.user;

import com.dmdev.domain.dto.user.request.UserCreateRequestDto;
import com.dmdev.mapper.user.UserCreateMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserCreateMapperTest {


    @Test
    void shouldReturnCorrectEntity() {
        UserCreateMapper mapper = new UserCreateMapper();

        var createUserRequestDto = new UserCreateRequestDto(
                "test@gmail.com", "test", "test",
                "vasia", "pupkin", "minsk",
                "+37529111111111", LocalDate.now().minusYears(20),
                "ak874", LocalDate.now().minusYears(6), LocalDate.now().plusYears(4)
        );

       var actualResult = mapper.map(createUserRequestDto);

        assertEquals(actualResult.getEmail(), createUserRequestDto.getEmail());
        assertEquals(actualResult.getLogin(), createUserRequestDto.getLogin());
        assertEquals(actualResult.getPassword(), createUserRequestDto.getPassword());
    }

}