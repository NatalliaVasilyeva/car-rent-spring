package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.domain.entity.UserDetails;
import integration.com.dmdev.IntegrationBaseTest;
import integration.com.dmdev.utils.builder.ExistEntityBuilder;
import integration.com.dmdev.utils.builder.TestEntityBuilder;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import static integration.com.dmdev.utils.TestEntityIdConst.TEST_DRIVER_LICENSE_ID_FOR_DELETE;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_DRIVER_LICENSE_ID;
import static integration.com.dmdev.utils.TestEntityIdConst.TEST_EXISTS_USER_DETAILS_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DriverLicenseTestIT extends IntegrationBaseTest {

    @Test
    void shouldCreateDriverLicense() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var userDetails = session.get(UserDetails.class, TEST_EXISTS_USER_DETAILS_ID);
            var driverLicenceToSave = TestEntityBuilder.createDriverLicense();
            userDetails.setDriverLicense(driverLicenceToSave);

            var savedDriverLicense = session.save(driverLicenceToSave);

            assertThat(savedDriverLicense).isNotNull();
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldReturnDriverLicense() {
        try (Session session = sessionFactory.openSession()) {
            var expectedDriverLicense = ExistEntityBuilder.getExistDriverLicense();

            var actualDriverLicense = session.find(DriverLicense.class, TEST_EXISTS_DRIVER_LICENSE_ID);

            assertThat(actualDriverLicense).isNotNull();
            assertEquals(expectedDriverLicense, actualDriverLicense);
        }
    }

    @Test
    void shouldUpdateDriverLicense() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var driverLicenseToUpdate = session.find(DriverLicense.class, TEST_EXISTS_DRIVER_LICENSE_ID);
            driverLicenseToUpdate.setNumber("dn36632");

            session.update(driverLicenseToUpdate);
            session.flush();
            session.evict(driverLicenseToUpdate);

            var updatedDriverLicense = session.find(DriverLicense.class, driverLicenseToUpdate.getId());

            assertThat(updatedDriverLicense).isEqualTo(driverLicenseToUpdate);
            session.getTransaction().rollback();
        }
    }

    @Test
    void shouldDeleteDriverLicense() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var driverLicenseToDelete = session.find(DriverLicense.class, TEST_DRIVER_LICENSE_ID_FOR_DELETE);

            session.delete(driverLicenseToDelete);

            assertThat(session.find(DriverLicense.class, driverLicenseToDelete.getId())).isNull();
            session.getTransaction().rollback();
        }
    }
}