package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.DriverLicense;
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

public class DriverLicenseTestIT extends IntegrationBaseTest {

    @Test
    public void shouldCreateDriverLicense() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long savedDriverLicenseId = (Long) session.save(FakeTestEntityBuilder.createDriverLicense());
            session.getTransaction().commit();

            assertEquals(CREATED_TEST_ENTITY_ID, savedDriverLicenseId);
        }
    }

    @Test
    public void shouldReturnDriverLicense() {
        try (Session session = sessionFactory.openSession()) {
            DriverLicense actualDriverLicense = session.find(DriverLicense.class, EXIST_TEST_ENTITY_ID);

            assertThat(actualDriverLicense).isNotNull();
            assertEquals(ExistTesEntityBuilder.getExistDriverLicense().getUserDetailsId(), actualDriverLicense.getUserDetailsId());
            assertEquals(ExistTesEntityBuilder.getExistDriverLicense().getIssueDate(), actualDriverLicense.getIssueDate());
            assertEquals(ExistTesEntityBuilder.getExistDriverLicense().getExpiredDate(), actualDriverLicense.getExpiredDate());
            assertEquals(ExistTesEntityBuilder.getExistDriverLicense().getNumber(), actualDriverLicense.getNumber());
        }
    }

    @Test
    public void shouldUpdateDriverLicense() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            DriverLicense driverLicenseToUpdate = ExistTesEntityBuilder.getUpdatedDriverLicense();
            session.update(driverLicenseToUpdate);
            session.getTransaction().commit();

            DriverLicense updatedDriverLicense = session.find(DriverLicense.class, driverLicenseToUpdate.getId());

            assertThat(updatedDriverLicense).isEqualTo(driverLicenseToUpdate);
        }
    }

    @Test
    public void shouldDeleteDriverLicense() {
        try (Session session = sessionFactory.openSession()) {
            DriverLicense driverLicenseToDelete = session.find(DriverLicense.class, DELETED_TEST_ENTITY_ID);
            session.beginTransaction();
            session.delete(driverLicenseToDelete);
            session.getTransaction().commit();

            assertThat(session.find(DriverLicense.class, driverLicenseToDelete.getId())).isNull();
        }
    }
}