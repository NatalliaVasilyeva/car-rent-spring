package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.User;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.ExistTesEntityBuilder;
import integration.com.dmdev.utils.builder.FakeTestEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static integration.com.dmdev.utils.TestEntityIdConst.CREATED_TEST_ENTITY_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.DELETED_TEST_ENTITY_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.EXIST_TEST_ENTITY_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTestIT extends IntegrationBaseTest {

    @Test
    public void shouldCreateUser() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long savedUserId = (Long) session.save(FakeTestEntityBuilder.createUser());
            session.getTransaction().commit();

            assertEquals(CREATED_TEST_ENTITY_ID, savedUserId);
        }
    }

    @Test
    public void shouldReturnUser() {
        try (Session session = sessionFactory.openSession()) {
            User actualUser = session.find(User.class, EXIST_TEST_ENTITY_ID);

            assertThat(actualUser).isNotNull();
            assertEquals(ExistTesEntityBuilder.getExistUser().getEmail(), actualUser.getEmail());
            assertEquals(ExistTesEntityBuilder.getExistUser().getLogin(), actualUser.getLogin());
            assertEquals(ExistTesEntityBuilder.getExistUser().getRole(), actualUser.getRole());
        }
    }

    @Test
    public void shouldUpdateUser() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User userToUpdate = ExistTesEntityBuilder.getUpdatedUser();
            session.update(userToUpdate);
            session.getTransaction().commit();

            User updatedUser = session.find(User.class, userToUpdate.getId());

            assertThat(updatedUser).isEqualTo(userToUpdate);
        }
    }

    @Test
    public void shouldDeleteUser() {
        try (Session session = sessionFactory.openSession()) {
            User userToDelete = session.find(User.class, DELETED_TEST_ENTITY_ID);
            session.beginTransaction();
            session.delete(userToDelete);
            session.getTransaction().commit();

            assertThat(session.find(User.class, userToDelete.getId())).isNull();
        }
    }
}