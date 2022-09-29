package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.UserDetails;
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

public class UserDetailsTestIT extends IntegrationBaseTest {

    @Test
    public void shouldCreateUserDetails() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long savedUserDetailsId = (Long) session.save(FakeTestEntityBuilder.createUserDetails());
            session.getTransaction().commit();

            assertEquals(CREATED_TEST_ENTITY_ID, savedUserDetailsId);
        }
    }

    @Test
    public void shouldReturnUserDetails() {
        try (Session session = sessionFactory.openSession()) {
            UserDetails actualUserDetails = session.find(UserDetails.class, EXIST_TEST_ENTITY_ID);

            assertThat(actualUserDetails).isNotNull();
            assertEquals(ExistTesEntityBuilder.getExistUserDetails().getName(), actualUserDetails.getName());
            assertEquals(ExistTesEntityBuilder.getExistUserDetails().getSurname(), actualUserDetails.getSurname());
            assertEquals(ExistTesEntityBuilder.getExistUserDetails().getAddress(), actualUserDetails.getAddress());
            assertEquals(ExistTesEntityBuilder.getExistUserDetails().getBirthday(), actualUserDetails.getBirthday());
            assertEquals(ExistTesEntityBuilder.getExistUserDetails().getPhone(), actualUserDetails.getPhone());
        }
    }

    @Test
    public void shouldUpdateUserDetails() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            UserDetails userDetailsToUpdate = ExistTesEntityBuilder.getUpdatedUserDetails();
            session.update(userDetailsToUpdate);
            session.getTransaction().commit();

            UserDetails updatedUserDetails = session.find(UserDetails.class, userDetailsToUpdate.getId());

            assertThat(updatedUserDetails).isEqualTo(userDetailsToUpdate);
        }
    }

    @Test
    public void shouldDeleteUserDetails() {
        try (Session session = sessionFactory.openSession()) {
            UserDetails userToDelete = session.find(UserDetails.class, DELETED_TEST_ENTITY_ID);
            session.beginTransaction();
            session.delete(userToDelete);
            session.getTransaction().commit();

            assertThat(session.find(UserDetails.class, userToDelete.getId())).isNull();
        }
    }
}