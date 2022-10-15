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
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryTestIT extends IntegrationBaseTest {

    private final UserRepository userRepository = UserRepository.getInstance();

    @Test
    void shouldReturnAllUsersWithHql() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<User> users = userRepository.findAllHQL(session);
            session.getTransaction().commit();

            assertThat(users).hasSize(2);

            List<String> emails = users.stream().map(User::getEmail).collect(toList());
            assertThat(emails).contains("admin@gmail.com", "client@gmail.com");

            List<LocalDate> birthdays = users.stream()
                    .map(User::getUserDetails)
                    .map(UserDetails::getBirthday)
                    .collect(toList());
            assertThat(birthdays).contains(LocalDate.of(1989, 3, 12), LocalDate.of(1986, 7, 2));
        }
    }

    @Test
    void shouldReturnAllUsersWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<User> users = userRepository.findAllCriteria(session);
            session.getTransaction().commit();

            assertThat(users).hasSize(2);

            List<String> emails = users.stream().map(User::getEmail).collect(toList());
            assertThat(emails).contains("admin@gmail.com", "client@gmail.com");

            List<LocalDate> birthdays = users.stream()
                    .map(User::getUserDetails)
                    .map(UserDetails::getBirthday)
                    .collect(toList());
            assertThat(birthdays).contains(LocalDate.of(1989, 3, 12), LocalDate.of(1986, 7, 2));
        }
    }

    @Test
    void shouldReturnAllUsersWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<User> users = userRepository.findAllQueryDsl(session);
            session.getTransaction().commit();

            assertThat(users).hasSize(2);

            List<String> emails = users.stream().map(User::getEmail).collect(toList());
            assertThat(emails).contains("admin@gmail.com", "client@gmail.com");

            List<LocalDate> birthdays = users.stream()
                    .map(User::getUserDetails)
                    .map(UserDetails::getBirthday)
                    .collect(toList());
            assertThat(birthdays).contains(LocalDate.of(1989, 3, 12), LocalDate.of(1986, 7, 2));
        }
    }

    @Test
    void shouldReturnUserByIdWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<User> optionalUser = userRepository.findByIdCriteria(session, TestEntityIdConst.TEST_EXISTS_USER_ID);
            session.getTransaction().commit();

            assertThat(optionalUser).isNotNull();
            optionalUser.ifPresent(user -> assertThat(user).isEqualTo(ExistEntityBuilder.getExistUser()));
        }
    }

    @Test
    void shouldReturnUsersByIdWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<User> optionalUser = userRepository.findByIdQueryDsl(session, TestEntityIdConst.TEST_EXISTS_USER_ID);
            session.getTransaction().commit();

            assertThat(optionalUser).isNotNull();
            optionalUser.ifPresent(user -> assertThat(user).isEqualTo(ExistEntityBuilder.getExistUser()));
        }
    }

    @Test
    void shouldReturnUserByEmailAndPasswordCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            UserFilter userFilter = UserFilter.builder()
                    .email("client@gmail.com")
                    .password("VasilechekBel123!")
                    .build();
            Optional<User> optionalUser = userRepository.findUsersByEmailAndPasswordCriteria(session, userFilter);
            session.getTransaction().commit();

            assertThat(optionalUser).isNotNull();
            optionalUser.ifPresent(user -> assertThat(user).isEqualTo(ExistEntityBuilder.getExistUser()));
        }
    }

    @Test
    void shouldReturnUserByEmailAndPasswordQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            UserFilter userFilter = UserFilter.builder()
                    .email("client@gmail.com")
                    .password("VasilechekBel123!")
                    .build();
            Optional<User> optionalUser = userRepository.findUsersByEmailAndPasswordQueryDsl(session, userFilter);
            session.getTransaction().commit();

            assertThat(optionalUser).isNotNull();
            optionalUser.ifPresent(user -> assertThat(user).isEqualTo(ExistEntityBuilder.getExistUser()));
        }
    }

    @Test
    void shouldReturnUserByBirthdayCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            UserFilter userFilter = UserFilter.builder()
                    .birthday(LocalDate.of(1989, 3, 12))
                    .build();
            List<User> users = userRepository.findUsersByBirthdayCriteria(session, userFilter);
            session.getTransaction().commit();

            assertThat(users).hasSize(1);
            assertThat(users.get(0).getUserDetails().getName()).isEqualTo("Petia");
            assertThat(users.get(0).getUserDetails().getSurname()).isEqualTo("Petrov");
        }
    }

    @Test
    void shouldReturnUserByBirthdayQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            UserFilter userFilter = UserFilter.builder()
                    .birthday(LocalDate.of(1989, 3, 12))
                    .build();
            List<User> users = userRepository.findUsersByBirthdayQueryDsl(session, userFilter);
            session.getTransaction().commit();

            assertThat(users).hasSize(1);
            assertThat(users.get(0).getUserDetails().getName()).isEqualTo("Petia");
            assertThat(users.get(0).getUserDetails().getSurname()).isEqualTo("Petrov");
        }
    }

    @Test
    void shouldReturnUsersWithShortDataOrderedByEmailCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<UserDto> users = userRepository.findUsersWithShortDataOrderedByEmailCriteria(session);
            session.getTransaction().commit();

            assertThat(users).hasSize(2);
            assertThat(users.get(0).getEmail()).isEqualTo("admin@gmail.com");
            List<String> phones = users.stream()
                    .map(UserDto::getPhone)
                    .collect(toList());
            assertThat(phones).contains("+375 29 124 56 78", "+375 29 124 56 79");
        }
    }

    @Test
    void shouldReturnUsersWithShortDataOrderedByEmailQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Tuple> users = userRepository.findUsersTupleWithShortDataOrderedByEmailQueryDsl(session);
            session.getTransaction().commit();

            assertThat(users).hasSize(2);
            List<String> emails = users.stream().map(r -> r.get(0, String.class)).collect(toList());
            assertThat(emails).contains("admin@gmail.com", "client@gmail.com");

            List<String> names = users.stream().map(r -> r.get(1, String.class)).collect(toList());
            assertThat(names).contains("Ivan", "Petia");

            List<String> phones = users.stream().map(r -> r.get(4, String.class)).collect(toList());
            assertThat(phones).contains("+375 29 124 56 78", "+375 29 124 56 79");
        }
    }

    @Test
    void shouldReturnUsersWithShortDataByNameOrSurnameAndBirthdayOrderedByEmailCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            UserFilter userFilter = UserFilter.builder()
                    .name("Ivan")
                    .surname("Petrov")
                    .birthday(LocalDate.of(1989, 3, 12))
                    .build();
            List<UserDto> users = userRepository.findUsersWithShortDataByNameOrSurnameAndBirthdayOrderedByEmailCriteria(session, userFilter);
            session.getTransaction().commit();

            assertThat(users).hasSize(1);
            assertThat(users.get(0).getName()).isEqualTo("Petia");
            assertThat(users.get(0).getPhone()).isEqualTo("+375 29 124 56 79");
        }
    }

    @Test
    void shouldReturnUsersWithShortDataByNameOrSurnameOrderedByEmailCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            UserFilter userFilter = UserFilter.builder()
                    .name("Ivan")
                    .surname("Petrov")
                    .build();
            List<UserDto> users = userRepository.findUsersWithShortDataByNameOrSurnameAndBirthdayOrderedByEmailCriteria(session, userFilter);
            session.getTransaction().commit();

            assertThat(users).hasSize(2);
            assertThat(users.get(0).getEmail()).isEqualTo("admin@gmail.com");
            assertThat(users.get(1).getEmail()).isEqualTo("client@gmail.com");
        }
    }

    @Test
    void shouldReturnUsersWithShortDataByNameOrSurnameAndBirthdayOrderedByEmailQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            UserFilter userFilter = UserFilter.builder()
                    .name("Ivan")
                    .surname("Petrov")
                    .birthday(LocalDate.of(1989, 3, 12))
                    .build();
            List<Tuple> users = userRepository.findUsersTupleByNameOrSurnameAndBirthdayOrderedByEmailQueryDsl(session, userFilter);
            session.getTransaction().commit();

            assertThat(users).hasSize(1);
            List<String> emails = users.stream().map(r -> r.get(0, String.class)).collect(toList());
            assertThat(emails).contains("client@gmail.com");
        }
    }
}