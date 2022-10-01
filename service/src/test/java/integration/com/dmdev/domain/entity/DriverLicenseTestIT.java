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
            UserDetails userDetails = session.get(UserDetails.class, TEST_EXISTS_USER_DETAILS_ID);
            DriverLicense driverLicenceToSave = TestEntityBuilder.createDriverLicense();
            userDetails.setDriverLicense(driverLicenceToSave);

            Long savedDriverLicenseId = (Long) session.save(driverLicenceToSave);
            session.getTransaction().commit();

            assertThat(savedDriverLicenseId).isNotNull();
        }
    }

    @Test
    void shouldReturnDriverLicense() {
        try (Session session = sessionFactory.openSession()) {
            DriverLicense expectedDriverLicense = ExistEntityBuilder.getExistDriverLicense();

            DriverLicense actualDriverLicense = session.find(DriverLicense.class, TEST_EXISTS_DRIVER_LICENSE_ID);

            assertThat(actualDriverLicense).isNotNull();
            assertEquals(expectedDriverLicense, actualDriverLicense);
        }
    }

    @Test
    void shouldUpdateDriverLicense() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            DriverLicense driverLicenseToUpdate = session.find(DriverLicense.class, TEST_EXISTS_DRIVER_LICENSE_ID);
            driverLicenseToUpdate.setNumber("dn36632");

            session.update(driverLicenseToUpdate);
            session.flush();
            session.evict(driverLicenseToUpdate);

            DriverLicense updatedDriverLicense = session.find(DriverLicense.class, driverLicenseToUpdate.getId());
            session.getTransaction().commit();

            assertThat(updatedDriverLicense).isEqualTo(driverLicenseToUpdate);
        }
    }

    @Test
    void shouldDeleteDriverLicense() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            DriverLicense driverLicenseToDelete = session.find(DriverLicense.class, TEST_DRIVER_LICENSE_ID_FOR_DELETE);

            session.delete(driverLicenseToDelete);
            session.getTransaction().commit();

            assertThat(session.find(DriverLicense.class, driverLicenseToDelete.getId())).isNull();
        }
    }
}