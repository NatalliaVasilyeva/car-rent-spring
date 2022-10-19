package integration.com.dmdev.repository;

import com.dmdev.domain.dto.UserDetailsFilter;
import com.dmdev.domain.entity.User;
import com.dmdev.domain.entity.UserContact;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.repository.UserDetailsRepository;
import com.querydsl.core.Tuple;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.TestEntityIdConst;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_USER_DETAILS_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_USER_DETAILS_ID_FOR_DELETE;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDetailsRepositoryTestIT extends IntegrationBaseTest {

    private final Session session = createProxySession(sessionFactory);
    private final UserDetailsRepository userDetailsRepository = new UserDetailsRepository(session);

    @Test
    void shouldFindByIdUserDetails() {
        session.beginTransaction();
        var expectedUserDetails = Optional.of(ExistEntityBuilder.getExistUserDetails());

        var actualUserDetails = userDetailsRepository.findById(TEST_EXISTS_USER_DETAILS_ID);

        assertThat(actualUserDetails).isNotNull();
        assertEquals(expectedUserDetails, actualUserDetails);
        session.getTransaction().rollback();
    }

    @Test
    void shouldUpdateUserDetails() {
        session.beginTransaction();
        var userContact = TestEntityBuilder.createUserContact();
        var userDetailsToUpdate = session.find(UserDetails.class, TEST_EXISTS_USER_DETAILS_ID);
        userDetailsToUpdate.setUserContact(userContact);

        userDetailsRepository.update(userDetailsToUpdate);
        session.clear();

        var updatedUserDetails = session.find(UserDetails.class, userDetailsToUpdate.getId());
        var updatedUser = session.find(User.class, userDetailsToUpdate.getUser().getId());

        assertThat(updatedUserDetails).isEqualTo(userDetailsToUpdate);
        assertThat(updatedUser.getUserDetails()).isEqualTo(updatedUserDetails);
        session.getTransaction().rollback();
    }

    @Test
    void shouldDeleteUserDetails() {
        session.beginTransaction();
        var userDetailsToDelete = session.find(UserDetails.class, TEST_USER_DETAILS_ID_FOR_DELETE);
        userDetailsToDelete.getUser().setUserDetails(null);

        userDetailsRepository.delete(TEST_USER_DETAILS_ID_FOR_DELETE);

        assertThat(session.find(UserDetails.class, TEST_USER_DETAILS_ID_FOR_DELETE)).isNull();
        session.getTransaction().rollback();
    }

    @Test
    void shouldFindAllUserDetails() {
        session.beginTransaction();

        List<UserDetails> userDetails = userDetailsRepository.findAll();
        assertThat(userDetails).hasSize(2);

        List<String> names = userDetails.stream().map(UserDetails::getName).collect(toList());
        assertThat(names).containsExactlyInAnyOrder("Ivan", "Petia");

        session.getTransaction().rollback();
    }


    @Test
    void shouldReturnAllUserDetailsWithHql() {
        session.beginTransaction();

        List<UserDetails> userDetails = userDetailsRepository.findAllHql();
        assertThat(userDetails).hasSize(2);

        List<String> names = userDetails.stream().map(UserDetails::getName).collect(toList());
        assertThat(names).contains("Ivan", "Petia");

        List<String> phones = userDetails.stream()
                .map(UserDetails::getUserContact)
                .map(UserContact::getPhone)
                .collect(toList());
        assertThat(phones).contains("+375 29 124 56 78", "+375 29 124 56 79");
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAllUserDetailsWithCriteria() {
        session.beginTransaction();

        List<UserDetails> userDetails = userDetailsRepository.findAllCriteria();
        assertThat(userDetails).hasSize(2);

        List<String> names = userDetails.stream().map(UserDetails::getName).collect(toList());
        assertThat(names).contains("Ivan", "Petia");

        List<String> phones = userDetails.stream()
                .map(UserDetails::getUserContact)
                .map(UserContact::getPhone)
                .collect(toList());
        assertThat(phones).contains("+375 29 124 56 78", "+375 29 124 56 79");
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAllUserDetailsWithQueryDsl() {
        session.beginTransaction();

        List<UserDetails> userDetails = userDetailsRepository.findAllQueryDsl();
        assertThat(userDetails).hasSize(2);

        List<String> names = userDetails.stream().map(UserDetails::getName).collect(toList());
        assertThat(names).contains("Ivan", "Petia");

        List<String> phones = userDetails.stream()
                .map(UserDetails::getUserContact)
                .map(UserContact::getPhone)
                .collect(toList());
        assertThat(phones).contains("+375 29 124 56 78", "+375 29 124 56 79");
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnUserDetailByIdWithCriteria() {
        session.beginTransaction();

        Optional<UserDetails> optionalUserDetails = userDetailsRepository.findByIdCriteria(TestEntityIdConst.TEST_EXISTS_USER_DETAILS_ID);

        assertThat(optionalUserDetails).isNotNull();
        optionalUserDetails.ifPresent(user -> assertThat(user.getId()).isEqualTo(ExistEntityBuilder.getExistUserDetails().getId()));
        assertThat(optionalUserDetails).isEqualTo(Optional.of(ExistEntityBuilder.getExistUserDetails()));
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnUserDetailByIdWithQueryDsl() {
        session.beginTransaction();

        Optional<UserDetails> optionalUserDetails = userDetailsRepository.findByIdQueryDsl(TestEntityIdConst.TEST_EXISTS_USER_DETAILS_ID);

        assertThat(optionalUserDetails).isNotNull();
        optionalUserDetails.ifPresent(user -> assertThat(user.getId()).isEqualTo(ExistEntityBuilder.getExistUserDetails().getId()));
        assertThat(optionalUserDetails).isEqualTo(Optional.of(ExistEntityBuilder.getExistUserDetails()));
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnUserDetailsByUserIdWithCriteria() {
        session.beginTransaction();

        Optional<UserDetails> optionalUserDetails = userDetailsRepository.findUserDetailsByUserIdCriteria(TestEntityIdConst.TEST_EXISTS_USER_ID);

        assertThat(optionalUserDetails).isNotNull();
        optionalUserDetails.ifPresent(user -> assertThat(user.getId()).isEqualTo(ExistEntityBuilder.getExistUserDetails().getId()));
        assertThat(optionalUserDetails).isEqualTo(Optional.of(ExistEntityBuilder.getExistUserDetails()));
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnUserDetailsByNameAndSurnameQueryDsl() {
        session.beginTransaction();
        UserDetailsFilter userDetailsFilter = UserDetailsFilter.builder()
                .name("Petia")
                .surname("Petrov")
                .build();

        List<UserDetails> userDetails = userDetailsRepository.findUserDetailsByNameAndSurnameQueryDsl(userDetailsFilter);

        assertThat(userDetails).hasSize(1);
        assertThat(userDetails.get(0)).isEqualTo(ExistEntityBuilder.getExistUserDetails());
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnUserDetailsUsersDetailsByBirthdayOrderedBySurnameAndNameQueryDsl() {
        session.beginTransaction();

        List<Tuple> userDetails = userDetailsRepository.findUsersDetailsTupleByBirthdayOrderedBySurnameAndNameQueryDsl(LocalDate.of(1989, 3, 12));
        assertThat(userDetails).hasSize(1);

        List<String> emails = userDetails.stream().map(r -> r.get(4, String.class)).collect(toList());
        assertThat(emails).contains("client@gmail.com");
        session.getTransaction().rollback();
    }
}