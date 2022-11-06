package integration.com.dmdev.repository;

import com.dmdev.domain.dto.UserDetailsFilter;
import com.dmdev.domain.entity.UserContact;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.repository.UserDetailsRepository;
import com.dmdev.repository.UserRepository;
import com.querydsl.core.Tuple;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_USER_DETAILS_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_USER_DETAILS_ID_FOR_DELETE;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDetailsRepositoryTestIT extends IntegrationBaseTest {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindByIdUserDetails() {
        var expectedUserDetails = Optional.of(ExistEntityBuilder.getExistUserDetails());

        var actualUserDetails = userDetailsRepository.findById(TEST_EXISTS_USER_DETAILS_ID);

        assertThat(actualUserDetails).isNotNull();
        assertEquals(expectedUserDetails, actualUserDetails);
    }

    @Test
    void shouldUpdateUserDetails() {
        var userContact = TestEntityBuilder.createUserContact();
        var userDetailsToUpdate = userDetailsRepository.findById(TEST_EXISTS_USER_DETAILS_ID).get();
        userDetailsToUpdate.setUserContact(userContact);

        userDetailsRepository.update(userDetailsToUpdate);

        var updatedUserDetails = userDetailsRepository.findById(userDetailsToUpdate.getId()).get();
        var updatedUser = userRepository.findById(userDetailsToUpdate.getUser().getId()).get();

        assertThat(updatedUserDetails).isEqualTo(userDetailsToUpdate);
        assertThat(updatedUser.getUserDetails()).isEqualTo(updatedUserDetails);
    }

    @Test
    void shouldDeleteUserDetails() {
        var userDetailsToDelete = userDetailsRepository.findById(TEST_USER_DETAILS_ID_FOR_DELETE);
        userDetailsToDelete.ifPresent(u -> u.getUser().setUserDetails(null));

        userDetailsToDelete.ifPresent(u -> userDetailsRepository.delete(u));

        assertThat(userDetailsRepository.findById(TEST_USER_DETAILS_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void shouldFindAllUserDetails() {
        List<UserDetails> userDetails = userDetailsRepository.findAll();
        assertThat(userDetails).hasSize(2);

        List<String> names = userDetails.stream().map(UserDetails::getName).collect(toList());
        assertThat(names).containsExactlyInAnyOrder("Ivan", "Petia");
    }

    @Test
    void shouldReturnAllUserDetailsWithQueryDsl() {
        List<UserDetails> userDetails = userDetailsRepository.findAllQueryDsl();
        assertThat(userDetails).hasSize(2);

        List<String> names = userDetails.stream().map(UserDetails::getName).collect(toList());
        assertThat(names).contains("Ivan", "Petia");

        List<String> phones = userDetails.stream()
                .map(UserDetails::getUserContact)
                .map(UserContact::getPhone)
                .collect(toList());
        assertThat(phones).contains("+375 29 124 56 78", "+375 29 124 56 79");
    }


    @Test
    void shouldReturnUserDetailByIdWithQueryDsl() {
        var optionalUserDetails = userDetailsRepository.findByIdQueryDsl(TestEntityIdConst.TEST_EXISTS_USER_DETAILS_ID);

        assertThat(optionalUserDetails).isNotNull();
        optionalUserDetails.ifPresent(user -> assertThat(user.getId()).isEqualTo(ExistEntityBuilder.getExistUserDetails().getId()));
        assertThat(optionalUserDetails).isEqualTo(Optional.of(ExistEntityBuilder.getExistUserDetails()));
    }

    @Test
    void shouldReturnUserDetailsByUserIdWithCriteria() {
        var optionalUserDetails = userDetailsRepository.findUserDetailsByUserIdCriteria(TestEntityIdConst.TEST_EXISTS_USER_ID);

        assertThat(optionalUserDetails).isNotNull();
        optionalUserDetails.ifPresent(user -> assertThat(user.getId()).isEqualTo(ExistEntityBuilder.getExistUserDetails().getId()));
        assertThat(optionalUserDetails).isEqualTo(Optional.of(ExistEntityBuilder.getExistUserDetails()));
    }

    @Test
    void shouldReturnUserDetailsByNameAndSurnameQueryDsl() {
        var userDetailsFilter = UserDetailsFilter.builder()
                .name("Petia")
                .surname("Petrov")
                .build();

        List<UserDetails> userDetails = userDetailsRepository.findUserDetailsByNameAndSurnameQueryDsl(userDetailsFilter);

        assertThat(userDetails).hasSize(1);
        assertThat(userDetails.get(0)).isEqualTo(ExistEntityBuilder.getExistUserDetails());
    }

    @Test
    void shouldReturnUserDetailsUsersDetailsByBirthdayOrderedBySurnameAndNameQueryDsl() {
        List<Tuple> userDetails = userDetailsRepository.findUsersDetailsTupleByBirthdayOrderedBySurnameAndNameQueryDsl(LocalDate.of(1989, 3, 12));
        assertThat(userDetails).hasSize(1);

        List<String> emails = userDetails.stream().map(r -> r.get(4, String.class)).collect(toList());
        assertThat(emails).contains("client@gmail.com");
    }
}