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
            UserDetails expectedUserDetails = ExistEntityBuilder.getExistUserDetails();

            UserDetails actualUserDetails = session.find(UserDetails.class, TEST_EXISTS_USER_DETAILS_ID);

            assertThat(actualUserDetails).isNotNull();
            assertEquals(expectedUserDetails, actualUserDetails);
        }
    }

    @Test
    void shouldUpdateUserDetails() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            UserContact userContact = TestEntityBuilder.createUserContact();
            UserDetails userDetailsToUpdate = session.find(UserDetails.class, TEST_EXISTS_USER_DETAILS_ID);
            userDetailsToUpdate.setUserContact(userContact);

            session.update(userDetailsToUpdate);
            session.flush();
            session.clear();

            UserDetails updatedUserDetails = session.find(UserDetails.class, userDetailsToUpdate.getId());
            User updatedUser = session.find(User.class, userDetailsToUpdate.getUser().getId());
            session.getTransaction().commit();

            assertThat(updatedUserDetails).isEqualTo(userDetailsToUpdate);
            assertThat(updatedUser.getUserDetails()).isEqualTo(updatedUserDetails);
        }
    }

    @Test
    void shouldDeleteUserDetails() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            UserDetails userDetailsToDelete = session.find(UserDetails.class, TEST_USER_DETAILS_ID_FOR_DELETE);
            userDetailsToDelete.getUser().setUserDetails(null);

            session.delete(userDetailsToDelete);
            session.getTransaction().commit();

            assertThat(session.find(UserDetails.class, userDetailsToDelete.getId())).isNull();
        }
    }
}