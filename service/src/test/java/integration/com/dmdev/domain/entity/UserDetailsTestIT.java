package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.User;
import com.dmdev.domain.entity.UserContact;
import com.dmdev.domain.entity.UserDetails;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_USER_DETAILS_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_USER_DETAILS_ID_FOR_DELETE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDetailsTestIT extends IntegrationBaseTest {

    @Test
    void shouldReturnUserDetails() {
        try (Session session = sessionFactory.openSession()) {
            var expectedUserDetails = ExistEntityBuilder.getExistUserDetails();

            var actualUserDetails = session.find(UserDetails.class, TEST_EXISTS_USER_DETAILS_ID);

            assertThat(actualUserDetails).isNotNull();
            assertEquals(expectedUserDetails, actualUserDetails);
        }
    }

    @Test
    void shouldUpdateUserDetails() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var userContact = TestEntityBuilder.createUserContact();
            var userDetailsToUpdate = session.find(UserDetails.class, TEST_EXISTS_USER_DETAILS_ID);
            userDetailsToUpdate.setUserContact(userContact);

            session.update(userDetailsToUpdate);
            session.flush();
            session.clear();

            var updatedUserDetails = session.find(UserDetails.class, userDetailsToUpdate.getId());
            var updatedUser = session.find(User.class, userDetailsToUpdate.getUser().getId());

            assertThat(updatedUserDetails).isEqualTo(userDetailsToUpdate);
            assertThat(updatedUser.getUserDetails()).isEqualTo(updatedUserDetails);
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldDeleteUserDetails() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var userDetailsToDelete = session.find(UserDetails.class, TEST_USER_DETAILS_ID_FOR_DELETE);
            userDetailsToDelete.getUser().setUserDetails(null);

            session.delete(userDetailsToDelete);

            assertThat(session.find(UserDetails.class, userDetailsToDelete.getId())).isNull();
            session.getTransaction().rollback();
        }
    }
}