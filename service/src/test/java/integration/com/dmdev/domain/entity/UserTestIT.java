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
            User userToSave = TestEntityBuilder.createUser();

            Long savedUserId = (Long) session.save(userToSave);
            session.getTransaction().commit();

            assertThat(savedUserId).isNotNull();
        }
    }

    @Test
    void shouldCreateUserWithUserDetails() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User userToSave = TestEntityBuilder.createUser();
            UserDetails userDetails = TestEntityBuilder.createUserDetails();
            userDetails.setUser(userToSave);

            Long savedUserId = (Long) session.save(userToSave);
            session.getTransaction().commit();

            assertThat(savedUserId).isNotNull();
            assertThat(userDetails).isNotNull();
        }
    }

    @Test
    void shouldReturnUser() {
        try (Session session = sessionFactory.openSession()) {
            User expectedUser = ExistEntityBuilder.getExistUser();

            User actualUser = session.find(User.class, TEST_EXISTS_USER_ID);

            assertThat(actualUser).isNotNull();
            assertEquals(expectedUser, actualUser);
        }
    }

    @Test
    void shouldUpdateUser() {
        String newAddress = "Hamburg";
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User userToUpdate = session.find(User.class, TEST_EXISTS_USER_ID);
            UserDetails userDetails = userToUpdate.getUserDetails();
            userDetails.getUserContact().setAddress(newAddress);
            userToUpdate.setPassword("8967562");
            userDetails.setUser(userToUpdate);

            session.update(userToUpdate);
            session.flush();
            session.clear();

            User updatedUser = session.find(User.class, userToUpdate.getId());
            session.getTransaction().commit();

            assertThat(updatedUser).isEqualTo(userToUpdate);
            assertThat(updatedUser.getUserDetails().getUserContact().getAddress()).isEqualTo(newAddress);
        }
    }

    @Test
    void shouldDeleteUser() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User userToDelete = session.find(User.class, TEST_USER_ID_FOR_DELETE);

            session.delete(userToDelete);
            session.getTransaction().commit();

            assertThat(session.find(User.class, userToDelete.getId())).isNull();
        }
    }
}