package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.User;
import com.dmdev.domain.entity.UserDetails;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_USER_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_USER_ID_FOR_DELETE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTestIT extends IntegrationBaseTest {

    @Test
    void shouldCreateUserWithoutUserDetails() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var userToSave = TestEntityBuilder.createUser();

            var savedUser = session.save(userToSave);

            assertThat(savedUser).isNotNull();
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldCreateUserWithUserDetails() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var userToSave = TestEntityBuilder.createUser();
            var userDetails = TestEntityBuilder.createUserDetails();
            userDetails.setUser(userToSave);

            var savedUserId = session.save(userToSave);

            assertThat(savedUserId).isNotNull();
            assertThat(userDetails).isNotNull();
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnUser() {
        try (Session session = sessionFactory.openSession()) {
            var expectedUser = ExistEntityBuilder.getExistUser();

            var actualUser = session.find(User.class, TEST_EXISTS_USER_ID);

            assertThat(actualUser).isNotNull();
            assertEquals(expectedUser, actualUser);
        }
    }

    @Test
    void shouldUpdateUser() {
        String newAddress = "Hamburg";
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var userToUpdate = session.find(User.class, TEST_EXISTS_USER_ID);
            var userDetails = userToUpdate.getUserDetails();
            userDetails.getUserContact().setAddress(newAddress);
            userToUpdate.setPassword("8967562");
            userDetails.setUser(userToUpdate);

            session.update(userToUpdate);
            session.flush();
            session.clear();

            var updatedUser = session.find(User.class, userToUpdate.getId());

            assertThat(updatedUser).isEqualTo(userToUpdate);
            assertThat(updatedUser.getUserDetails().getUserContact().getAddress()).isEqualTo(newAddress);
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldDeleteUser() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var userToDelete = session.find(User.class, TEST_USER_ID_FOR_DELETE);

            session.delete(userToDelete);

            assertThat(session.find(User.class, userToDelete.getId())).isNull();
            session.getTransaction().rollback();
        }
    }
}