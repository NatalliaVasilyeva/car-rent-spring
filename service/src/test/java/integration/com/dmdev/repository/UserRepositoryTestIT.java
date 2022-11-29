package integration.com.dmdev.repository;

import com.dmdev.domain.dto.filterdto.UserFilter;
import com.dmdev.domain.entity.User;
import com.dmdev.domain.model.Role;
import com.dmdev.repository.UserRepository;
import com.dmdev.utils.predicate.QPredicate;
import com.dmdev.utils.predicate.UserPredicateBuilder;
import com.querydsl.core.types.Predicate;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.dmdev.domain.entity.QUser.user;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_USER_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_USER_ID_FOR_DELETE;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRepositoryTestIT extends IntegrationBaseTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPredicateBuilder userPredicateBuilder;

    @Test
    void shouldSaveUserWithoutUserDetails() {
        var userToSave = TestEntityBuilder.createUser();

        var savedUser = userRepository.save(userToSave);

        assertThat(savedUser).isNotNull();
    }

    @Test
    void shouldSaveUserWithUserDetails() {
        var userToSave = TestEntityBuilder.createUser();
        var userDetails = TestEntityBuilder.createUserDetails();
        userDetails.setUser(userToSave);

        var savedUser = userRepository.save(userToSave);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(userDetails).isNotNull();
    }

    @Test
    void shouldFindByIdUser() {
        var expectedUser = Optional.of(ExistEntityBuilder.getExistUser());

        var actualUser = userRepository.findById(TEST_EXISTS_USER_ID);

        assertThat(actualUser).isNotNull();
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void shouldUpdateUser() {
        var userToUpdate = userRepository.findById(TEST_EXISTS_USER_ID).get();
        var userDetails = userToUpdate.getUserDetails();
        userToUpdate.setPassword("8967562");
        userDetails.setUser(userToUpdate);

        userRepository.save(userToUpdate);

        var updatedUser = userRepository.findById(userToUpdate.getId()).get();

        assertThat(updatedUser).isEqualTo(userToUpdate);
    }

    @Test
    void shouldDeleteUser() {
        var user = userRepository.findById(TEST_USER_ID_FOR_DELETE);

        user.ifPresent(u -> userRepository.delete(u));

        assertThat(userRepository.findById(TEST_USER_ID_FOR_DELETE)).isEmpty();
    }

    @Test
    void shouldFindAllUsers() {
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(2);

        List<String> emails = users.stream().map(User::getEmail).collect(toList());
        assertThat(emails).containsExactlyInAnyOrder("admin@gmail.com", "client@gmail.com");
    }


    @Test
    void shouldReturnUserByEmailAndPasswordWithFilter() {
        var userFilter = UserFilter.builder()
                .email("client@gmail.com")
                .build();

        var optionalUser = userRepository.findOne(userPredicateBuilder.build(userFilter));

        assertThat(optionalUser).isNotNull();
        optionalUser.ifPresent(user -> assertThat(user.getId()).isEqualTo(ExistEntityBuilder.getExistUser().getId()));
        assertThat(optionalUser).isEqualTo(Optional.of(ExistEntityBuilder.getExistUser()));
    }

    @Test
    void shouldReturnUserByEmailAndPassword() {
        var optionalUser = userRepository.findByEmailAndPassword("client@gmail.com", "{noop}TestTest1234!");

        assertThat(optionalUser).isNotNull();
        optionalUser.ifPresent(user -> assertThat(user.getId()).isEqualTo(ExistEntityBuilder.getExistUser().getId()));
        assertThat(optionalUser).isEqualTo(Optional.of(ExistEntityBuilder.getExistUser()));
    }

    @Test
    void shouldReturnUserByEmail() {
        var optionalUser = userRepository.findByEmail("client@gmail.com");

        assertThat(optionalUser).isNotNull();
        optionalUser.ifPresent(user -> assertThat(user.getId()).isEqualTo(ExistEntityBuilder.getExistUser().getId()));
        assertThat(optionalUser).isEqualTo(Optional.of(ExistEntityBuilder.getExistUser()));
    }

    @Test
    void shouldReturnUserByPhone() {
        var optionalUser = userRepository.findByPhone("+375 29 124 56 79");

        assertThat(optionalUser).isNotEmpty();
        optionalUser.ifPresent(user -> assertEquals(user, ExistEntityBuilder.getExistUser()));
    }

    @Test
    void shouldReturnUsersWithOrders() {
        List<User> users = userRepository.findAllWithOrders();

        assertThat(users).isNotEmpty().hasSize(2);
        List<String> emails = users.stream().map(User::getEmail).collect(toList());
        assertThat(emails).containsExactlyInAnyOrder("admin@gmail.com", "client@gmail.com");
    }

    @Test
    void shouldReturnUsersWithoutOrders() {
        List<User> users = userRepository.findAllWithoutOrders();

        assertThat(users).isEmpty();
    }

    @Test
    void shouldReturnUserByRole() {
        var users = userRepository.findAllByRole(Role.CLIENT);

        assertThat(users).hasSize(1);

        List<String> emails = users.stream().map(User::getEmail).collect(toList());
        assertThat(emails).containsExactlyInAnyOrder("client@gmail.com");
    }

    @Test
    void shouldReturnUsersByRegistrationDate() {
        var users = userRepository.findAllByRegistrationDate(LocalDate.of(2022, 9, 22));

        assertThat(users).hasSize(2);

        List<String> emails = users.stream().map(User::getEmail).collect(toList());
        assertThat(emails).containsExactlyInAnyOrder("admin@gmail.com", "client@gmail.com");
    }

    @Test
    void shouldReturnUserByUserFilterWithBirthday() {
        var userFilter = UserFilter.builder()
                .birthday(LocalDate.of(1989, 3, 12))
                .build();

        Iterable<User> users = userRepository.findAll(userPredicateBuilder.build(userFilter));

        assertThat(users).hasSize(1);
        assertThat(users.iterator().next().getUserDetails().getName()).isEqualTo("Petia");
        assertThat(users.iterator().next().getUserDetails().getSurname()).isEqualTo("Petrov");
    }


    @Test
    void shouldReturnUsersByUserFilterWithNameOrSurnameAndBirthdayOrderedByEmail() {
        var userFilter = UserFilter.builder()
                .name("Ivan")
                .surname("Petrov")
                .birthday(LocalDate.of(1989, 3, 12))
                .build();

        Predicate orPredicates = QPredicate.builder()
                .add(userFilter.getName(), user.userDetails.name::eq)
                .add(userFilter.getSurname(), user.userDetails.surname::eq)
                .buildOr();

        Predicate birthdayPredicates = QPredicate.builder()
                .add(userFilter.getBirthday(), user.userDetails.birthday::eq)
                .buildAnd();

        Predicate resultPredicates = QPredicate.builder()
                .addPredicate(orPredicates)
                .addPredicate(birthdayPredicates)
                .buildAnd();

        Sort sort = Sort.by("email").descending();
        Iterable<User> users = userRepository.findAll(resultPredicates, sort);

        assertThat(users).hasSize(1);
        List<String> emails = StreamSupport.stream(users.spliterator(), false).map(User::getEmail).collect(toList());
        assertThat(emails).contains("client@gmail.com");
    }
}