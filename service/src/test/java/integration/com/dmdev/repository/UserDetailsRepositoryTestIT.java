package integration.com.dmdev.repository;

import com.dmdev.domain.dto.filterdto.UserDetailsFilter;
import com.dmdev.domain.entity.User;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.repository.UserDetailsRepository;
import com.dmdev.repository.UserRepository;
import com.dmdev.utils.predicate.UserDetailsPredicateBuilder;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

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

    @Autowired
    private UserDetailsPredicateBuilder userDetailsPredicateBuilder;

    @Test
    void shouldFindById() {
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

        userDetailsRepository.save(userDetailsToUpdate);

        var updatedUserDetails = userDetailsRepository.findById(userDetailsToUpdate.getId()).get();
        var updatedUser = userRepository.findById(userDetailsToUpdate.getUser().getId()).get();

        assertThat(updatedUserDetails).isEqualTo(userDetailsToUpdate);
        assertThat(updatedUser.getUserDetails()).isEqualTo(updatedUserDetails);
    }

    @Test
    void shouldDeleteUserDetails() {
        var userDetailsToDelete = userDetailsRepository.findById(TEST_USER_DETAILS_ID_FOR_DELETE);

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
    void shouldReturnUserDetailsByUserId() {
        var optionalUserDetails = userDetailsRepository.findByUserId(TestEntityIdConst.TEST_EXISTS_USER_ID);

        assertThat(optionalUserDetails).isNotNull();
        optionalUserDetails.ifPresent(user -> assertThat(user.getId()).isEqualTo(ExistEntityBuilder.getExistUserDetails().getId()));
        assertThat(optionalUserDetails).isEqualTo(Optional.of(ExistEntityBuilder.getExistUserDetails()));
    }

    @Test
    void shouldReturnUserDetailsByNameAndSurnameWithFilter() {
        var userDetailsFilter = UserDetailsFilter.builder()
                .name("Petia")
                .surname("Petrov")
                .build();

        List<UserDetails> userDetails = IterableUtils.toList(userDetailsRepository.findAll(userDetailsPredicateBuilder.build(userDetailsFilter)));

        assertThat(userDetails).hasSize(1);
        assertThat(userDetails.get(0)).isEqualTo(ExistEntityBuilder.getExistUserDetails());
    }

    @Test
    void shouldReturnUserDetailsByNameAndSurnameWithoutFilter() {
        List<UserDetails> userDetails = userDetailsRepository.findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase("Petia", "Petrov");

        assertThat(userDetails).hasSize(1);
        assertThat(userDetails.get(0)).isEqualTo(ExistEntityBuilder.getExistUserDetails());
    }

    @Test
    void shouldReturnUserDetailsByBirthdayOrderedBySurnameAndName() {
        var userDetailsFilter = UserDetailsFilter.builder()
                .birthday(LocalDate.of(1989, 3, 12))
                .build();
        Sort sort = Sort.by("surname").descending().by("name").descending();
        List<UserDetails> userDetails = IterableUtils.toList(userDetailsRepository.findAll(userDetailsPredicateBuilder.build(userDetailsFilter), sort));
        assertThat(userDetails).hasSize(1);

        List<String> emails = userDetails.stream().map(UserDetails::getUser).map(User::getEmail).collect(toList());
        assertThat(emails).contains("client@gmail.com");
    }

    @Test
    void shouldNotReturnUserDetailsByRegistrationDate() {
        List<UserDetails> userDetails = userDetailsRepository.findByRegistrationDate(LocalDate.of(2022, 9, 21));

        assertThat(userDetails).isEmpty();
    }

    @Test
    void shouldReturnUserDetailsByRegistrationDate() {
        List<UserDetails> userDetails = userDetailsRepository.findByRegistrationDate(LocalDate.of(2022, 9, 22));

        assertThat(userDetails).hasSize(2);
    }

    @Test
    void shouldReturnUserDetailsByRegistrationDates() {
        List<UserDetails> userDetails = userDetailsRepository.findByRegistrationDateBetween(LocalDate.of(2022, 9, 21), LocalDate.of(2022, 9, 22));

        assertThat(userDetails).hasSize(2);
    }
}