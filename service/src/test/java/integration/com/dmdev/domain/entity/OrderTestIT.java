package integration.com.dmdev.domain.entity;

import com.dmdev.domain.entity.Order;
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

public class OrderTestIT extends IntegrationBaseTest {

    @Test
    public void shouldCreateOrder() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Long savedOrderId = (Long) session.save(FakeTestEntityBuilder.createOrder());
            session.getTransaction().commit();

            assertEquals(CREATED_TEST_ENTITY_ID, savedOrderId);
        }
    }

    @Test
    public void shouldReturnOrder() {
        try (Session session = sessionFactory.openSession()) {
            Order actualOrder = session.find(Order.class, EXIST_TEST_ENTITY_ID);

            assertThat(actualOrder).isNotNull();
            assertEquals(ExistTesEntityBuilder.getExistOrder().getId(), actualOrder.getId());
            assertEquals(ExistTesEntityBuilder.getExistOrder().getCarId(), actualOrder.getCarId());
            assertEquals(ExistTesEntityBuilder.getExistOrder().getOrderStatus(), actualOrder.getOrderStatus());
            assertEquals(ExistTesEntityBuilder.getExistOrder().getDate(), actualOrder.getDate());
            assertEquals(ExistTesEntityBuilder.getExistOrder().getInsurance(), actualOrder.getInsurance());
            assertEquals(ExistTesEntityBuilder.getExistOrder().getSum(), actualOrder.getSum());
        }
    }

    @Test
    public void shouldUpdateOrder() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Order orderToUpdate = ExistTesEntityBuilder.getUpdatedOrder();
            session.update(orderToUpdate);
            session.getTransaction().commit();

            Order updatedOrder = session.find(Order.class, orderToUpdate.getId());

            assertThat(updatedOrder).isEqualTo(orderToUpdate);
        }
    }

    @Test
    public void shouldDeleteOrder() {
        try (Session session = sessionFactory.openSession()) {
            Order orderToDelete = session.find(Order.class, DELETED_TEST_ENTITY_ID);
            session.beginTransaction();
            session.delete(orderToDelete);
            session.getTransaction().commit();

            assertThat(session.find(Order.class, orderToDelete.getId())).isNull();
        }
    }
}