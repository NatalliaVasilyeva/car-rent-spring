package com.dmdev.repository;

import com.dmdev.domain.dto.OrderFilter;
import com.dmdev.domain.entity.Car_;
import com.dmdev.domain.entity.Order;
import com.dmdev.domain.entity.Order_;
import com.dmdev.domain.model.OrderStatus;
import com.dmdev.utils.predicate.QPredicate;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QAccident.accident;
import static com.dmdev.domain.entity.QBrand.brand;
import static com.dmdev.domain.entity.QCar.car;
import static com.dmdev.domain.entity.QModel.model;
import static com.dmdev.domain.entity.QOrder.order;

public class OrderRepository implements Repository<Long, Order> {
    private static final OrderRepository INSTANCE = new OrderRepository();

    public static OrderRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Order> findAllHql(Session session) {
        return session.createQuery("select o from Order o", Order.class)
                .list();
    }

    @Override
    public List<Order> findAllCriteria(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Order.class);
        var order = criteria.from(Order.class);

        criteria.select(order);

        return session.createQuery(criteria)
                .list();
    }

    @Override
    public List<Order> findAllQueryDsl(Session session) {
        return new JPAQuery<Order>(session)
                .select(order)
                .from(order)
                .fetch();
    }

    @Override
    public Optional<Order> findByIdCriteria(Session session, Long id) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Order.class);
        var order = criteria.from(Order.class);

        criteria.select(order)
                .where(cb.equal(order.get(Order_.id), id));

        return Optional.ofNullable(session.createQuery(criteria).uniqueResult());
    }

    @Override
    public Optional<Order> findByIdQueryDsl(Session session, Long id) {
        return Optional.ofNullable(new JPAQuery<Order>(session)
                .select(order)
                .from(order)
                .where(order.id.eq(id))
                .fetchOne());
    }

    public List<Order> findOrdersByCarNumberCriteria(Session session, String carNumber) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Order.class);
        var order = criteria.from(Order.class);
        var car = order.join(Order_.car);

        criteria.select(order)
                .where(cb.equal(car.get(Car_.carNumber), carNumber));

        return session.createQuery(criteria).list();
    }

    public List<Order> findOrdersByOrderStatusCriteria(Session session, OrderStatus orderStatus) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Order.class);
        var order = criteria.from(Order.class);

        criteria.select(order)
                .where(cb.equal(order.get(Order_.orderStatus), orderStatus));

        return session.createQuery(criteria).list();
    }

    public List<Tuple> findOrderTuplesWithAvgSumAndDateOrderByDateQueryDsl(Session session) {
        return new JPAQuery<Tuple>(session)
                .select(order.date, order.sum.avg())
                .from(order)
                .groupBy(order.date)
                .orderBy(order.date.asc())
                .fetch();
    }

    public List<Order> findOrdersByBrandNameAndModelNameOrderByDateQueryDsl(Session session, OrderFilter orderFilter) {
        var predicates = QPredicate.builder()
                .add(orderFilter.getModelName(), model.name::eq)
                .add(orderFilter.getBrandName(), brand.name::eq)
                .buildAnd();

        return new JPAQuery<Order>(session)
                .select(order)
                .from(order)
                .join(order.car, car)
                .join(car.model, model)
                .join(model.brand, brand)
                .where(predicates)
                .orderBy(order.date.asc())
                .fetch();
    }

    public List<Order> findOrdersWhereAccidentsSumMoreThanAvgSumOrderByDateQueryDsl(Session session) {
        return new JPAQuery<Order>(session)
                .select(order)
                .from(order)
                .join(order.accidents, accident)
                .groupBy(order.id)
                .having(accident.damage.avg().gt(
                        new JPAQuery<BigDecimal>(session)
                                .select(accident.damage.avg())
                                .from(accident)
                ))
                .orderBy(order.date.asc())
                .fetch();
    }
}