package integration.com.dmdev.repository;

import com.dmdev.domain.dto.UserDetailsFilter;
import com.dmdev.domain.entity.UserContact;
import com.dmdev.domain.entity.UserDetails;
import com.dmdev.repository.UserDetailsRepository;
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

class UserDetailsRepositoryTestIT extends IntegrationBaseTest {

    private final UserDetailsRepository userDetailsRepository = UserDetailsRepository.getInstance();

    @Test
    void shouldReturnAllUserDetailsWithHql() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<UserDetails> userDetails = userDetailsRepository.findAllHql(session);

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
    }

    @Test
    void shouldReturnAllUserDetailsWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<UserDetails> userDetails = userDetailsRepository.findAllCriteria(session);

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
    }

    @Test
    void shouldReturnAllUserDetailsWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<UserDetails> userDetails = userDetailsRepository.findAllQueryDsl(session);

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
    }

    @Test
    void shouldReturnUserDetailByIdWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<UserDetails> optionalUserDetails = userDetailsRepository.findByIdCriteria(session, TestEntityIdConst.TEST_EXISTS_USER_DETAILS_ID);

            assertThat(optionalUserDetails).isNotNull();
            optionalUserDetails.ifPresent(user -> assertThat(user.getId()).isEqualTo(ExistEntityBuilder.getExistUserDetails().getId()));
            assertThat(optionalUserDetails).isEqualTo(Optional.of(ExistEntityBuilder.getExistUserDetails()));
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnUserDetailByIdWithQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<UserDetails> optionalUserDetails = userDetailsRepository.findByIdQueryDsl(session, TestEntityIdConst.TEST_EXISTS_USER_DETAILS_ID);

            assertThat(optionalUserDetails).isNotNull();
            optionalUserDetails.ifPresent(user -> assertThat(user.getId()).isEqualTo(ExistEntityBuilder.getExistUserDetails().getId()));
            assertThat(optionalUserDetails).isEqualTo(Optional.of(ExistEntityBuilder.getExistUserDetails()));
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnUserDetailsByUserIdWithCriteria() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<UserDetails> optionalUserDetails = userDetailsRepository.findUserDetailsByUserIdCriteria(session, TestEntityIdConst.TEST_EXISTS_USER_ID);

            assertThat(optionalUserDetails).isNotNull();
            optionalUserDetails.ifPresent(user -> assertThat(user.getId()).isEqualTo(ExistEntityBuilder.getExistUserDetails().getId()));
            assertThat(optionalUserDetails).isEqualTo(Optional.of(ExistEntityBuilder.getExistUserDetails()));
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnUserDetailsByNameAndSurnameQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            UserDetailsFilter userDetailsFilter = UserDetailsFilter.builder()
                    .name("Petia")
                    .surname("Petrov")
                    .build();
            List<UserDetails> userDetails = userDetailsRepository.findUserDetailsByNameAndSurnameQueryDsl(session, userDetailsFilter);

            assertThat(userDetails).hasSize(1);
            assertThat(userDetails.get(0)).isEqualTo(ExistEntityBuilder.getExistUserDetails());
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnUserDetailsUsersDetailsByBirthdayOrderedBySurnameAndNameQueryDsl() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<Tuple> userDetails = userDetailsRepository.findUsersDetailsTupleByBirthdayOrderedBySurnameAndNameQueryDsl(session, LocalDate.of(1989, 3, 12));

            assertThat(userDetails).hasSize(1);

            List<String> emails = userDetails.stream().map(r -> r.get(4, String.class)).collect(toList());
            assertThat(emails).contains("client@gmail.com");
            session.getTransaction().rollback();
        }
    }
}