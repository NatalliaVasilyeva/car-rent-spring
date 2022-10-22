package integration.com.dmdev.repository;

import com.dmdev.domain.dto.UserDto;
import com.dmdev.domain.dto.UserFilter;
import com.dmdev.domain.entity.User;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.repository.UserRepository;
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

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_USER_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_USER_ID_FOR_DELETE;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRepositoryTestIT extends IntegrationBaseTest {

    private final Session session = context.getBean(Session.class);
    private final UserRepository userRepository = context.getBean(UserRepository.class);

    @Test
    void shouldSaveUserWithoutUserDetails() {
        session.beginTransaction();
        var userToSave = TestEntityBuilder.createUser();

        var savedUser = userRepository.save(userToSave);

        assertThat(savedUser).isNotNull();
        session.getTransaction().rollback();
    }

    @Test
    void shouldSaveUserWithUserDetails() {
        session.beginTransaction();
        var userToSave = TestEntityBuilder.createUser();
        var userDetails = TestEntityBuilder.createUserDetails();
        userDetails.setUser(userToSave);

        var savedUser = userRepository.save(userToSave);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(userDetails).isNotNull();
        session.getTransaction().rollback();
    }

    @Test
    void shouldFindByIdUser() {
        session.beginTransaction();
        var expectedUser = Optional.of(ExistEntityBuilder.getExistUser());

        var actualUser = userRepository.findById(TEST_EXISTS_USER_ID);

        assertThat(actualUser).isNotNull();
        assertEquals(expectedUser, actualUser);
        session.getTransaction().rollback();
    }

    @Test
    void shouldUpdateUser() {
        session.beginTransaction();
        var userToUpdate = userRepository.findById(TEST_EXISTS_USER_ID).get();
        var userDetails = userToUpdate.getUserDetails();
        userToUpdate.setPassword("8967562");
        userDetails.setUser(userToUpdate);

        userRepository.update(userToUpdate);
        session.clear();

        var updatedUser = userRepository.findById(userToUpdate.getId()).get();

        assertThat(updatedUser).isEqualTo(userToUpdate);
        session.getTransaction().rollback();
    }

    @Test
    void shouldDeleteUser() {
        session.beginTransaction();
        var user = userRepository.findById(TEST_USER_ID_FOR_DELETE);

        user.ifPresent(u -> userRepository.delete(u));

        assertThat(userRepository.findById(TEST_USER_ID_FOR_DELETE)).isEmpty();
        session.getTransaction().rollback();
    }

    @Test
    void shouldFindAllAccidents() {
        session.beginTransaction();

        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(2);

        List<String> emails = users.stream().map(User::getEmail).collect(toList());
        assertThat(emails).containsExactlyInAnyOrder("admin@gmail.com", "client@gmail.com");

        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnAllUsersWithQueryDsl() {
        session.beginTransaction();

        List<User> users = userRepository.findAllQueryDsl();
        assertThat(users).hasSize(2);

        List<String> emails = users.stream().map(User::getEmail).collect(toList());
        assertThat(emails).contains("admin@gmail.com", "client@gmail.com");

        List<LocalDate> birthdays = users.stream()
                .map(User::getUserDetails)
                .map(UserDetails::getBirthday)
                .collect(toList());
        assertThat(birthdays).contains(LocalDate.of(1989, 3, 12), LocalDate.of(1986, 7, 2));
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnUsersByIdWithQueryDsl() {
        session.beginTransaction();

        var optionalUser = userRepository.findByIdQueryDsl(TestEntityIdConst.TEST_EXISTS_USER_ID);

        assertThat(optionalUser).isNotNull();
        optionalUser.ifPresent(user -> assertThat(user.getId()).isEqualTo(ExistEntityBuilder.getExistUser().getId()));
        assertThat(optionalUser).isEqualTo(Optional.of(ExistEntityBuilder.getExistUser()));
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnUserByEmailAndPasswordQueryDsl() {
        session.beginTransaction();
        var userFilter = UserFilter.builder()
                .email("client@gmail.com")
                .password("VasilechekBel123!")
                .build();

        var optionalUser = userRepository.findUsersByEmailAndPasswordQueryDsl(userFilter);

        assertThat(optionalUser).isNotNull();
        optionalUser.ifPresent(user -> assertThat(user.getId()).isEqualTo(ExistEntityBuilder.getExistUser().getId()));
        assertThat(optionalUser).isEqualTo(Optional.of(ExistEntityBuilder.getExistUser()));
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnUserByBirthdayQueryDsl() {
        session.beginTransaction();
        var userFilter = UserFilter.builder()
                .birthday(LocalDate.of(1989, 3, 12))
                .build();

        List<User> users = userRepository.findUsersByBirthdayQueryDsl(userFilter);

        assertThat(users).hasSize(1);
        assertThat(users.get(0).getUserDetails().getName()).isEqualTo("Petia");
        assertThat(users.get(0).getUserDetails().getSurname()).isEqualTo("Petrov");
        session.getTransaction().rollback();
    }

    @Test
    void shouldReturnUsersWithShortDataOrderedByEmailCriteria() {
        session.beginTransaction();

        List<UserDto> users = userRepository.findUsersWithShortDataOrderedByEmailCriteria();

        assertThat(users).hasSize(2);
        assertThat(users.get(0).getEmail()).isEqualTo("admin@gmail.com");
        List<String> phones = users.stream()
                .map(UserDto::getPhone)
                .collect(toList());
        assertThat(phones).contains("+375 29 124 56 78", "+375 29 124 56 79");
        session.getTransaction().rollback();
    }


    @Test
    void shouldReturnUsersWithShortDataByNameOrSurnameAndBirthdayOrderedByEmailQueryDsl() {
        session.beginTransaction();
        var userFilter = UserFilter.builder()
                .name("Ivan")
                .surname("Petrov")
                .birthday(LocalDate.of(1989, 3, 12))
                .build();

        List<Tuple> users = userRepository.findUsersTupleByNameOrSurnameAndBirthdayOrderedByEmailQueryDsl(userFilter);

        assertThat(users).hasSize(1);
        List<String> emails = users.stream().map(r -> r.get(0, String.class)).collect(toList());
        assertThat(emails).contains("client@gmail.com");
        session.getTransaction().rollback();
    }
}