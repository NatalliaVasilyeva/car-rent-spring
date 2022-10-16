package com.dmdev.repository;

import com.dmdev.domain.dto.OrderFilter;
import com.dmdev.domain.entity.Car_;
import com.dmdev.domain.entity.Order;
import com.dmdev.domain.entity.Order_;
import com.dmdev.domain.model.OrderStatus;
import com.dmdev.utils.predicate.QPredicate;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QAccident.accident;
import static com.dmdev.domain.entity.QBrand.brand;
import static com.dmdev.domain.entity.QCar.car;
import static com.dmdev.domain.entity.QModel.model;
import static com.dmdev.domain.entity.QOrder.order;

public class OrderRepository extends BaseRepository<Long, Order> {

    public OrderRepository(EntityManager entityManager) {
        super(Order.class, entityManager);
    }

    public List<Order> findAllHql() {
        return getEntityManager().createQuery("select o from Order o", Order.class)
                .getResultList();
    }

    public List<Order> findAllCriteria() {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Order.class);
        var order = criteria.from(Order.class);

        criteria.select(order);

        return getEntityManager().createQuery(criteria)
                .getResultList();
    }

    public List<Order> findAllQueryDsl() {
        return new JPAQuery<Order>(getEntityManager())
                .select(order)
                .from(order)
                .fetch();
    }

    public Optional<Order> findByIdCriteria(Long id) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Order.class);
        var order = criteria.from(Order.class);

        criteria.select(order)
                .where(cb.equal(order.get(Order_.id), id));

        return Optional.ofNullable(getEntityManager().createQuery(criteria).getSingleResult());
    }

    public Optional<Order> findByIdQueryDsl(Long id) {
        return Optional.ofNullable(new JPAQuery<Order>(getEntityManager())
                .select(order)
                .from(order)
                .where(order.id.eq(id))
                .fetchOne());
    }

    public List<Order> findOrdersByCarNumberCriteria(String carNumber) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Order.class);
        var order = criteria.from(Order.class);
        var car = order.join(Order_.car);

        criteria.select(order)
                .where(cb.equal(car.get(Car_.carNumber), carNumber));

        return getEntityManager().createQuery(criteria).getResultList();
    }

    public List<Order> findOrdersByOrderStatusCriteria(OrderStatus orderStatus) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Order.class);
        var order = criteria.from(Order.class);

        criteria.select(order)
                .where(cb.equal(order.get(Order_.orderStatus), orderStatus));

        return getEntityManager().createQuery(criteria).getResultList();
    }

    public List<Tuple> findOrderTuplesWithAvgSumAndDateOrderByDateQueryDsl() {
        return new JPAQuery<Tuple>(getEntityManager())
                .select(order.date, order.sum.avg())
                .from(order)
                .groupBy(order.date)
                .orderBy(order.date.asc())
                .fetch();
    }

    public List<Order> findOrdersByBrandNameAndModelNameOrderByDateQueryDsl(OrderFilter orderFilter) {
        var predicates = QPredicate.builder()
                .add(orderFilter.getModelName(), model.name::eq)
                .add(orderFilter.getBrandName(), brand.name::eq)
                .buildAnd();

        return new JPAQuery<Order>(getEntityManager())
                .select(order)
                .from(order)
                .join(order.car, car)
                .join(car.model, model)
                .join(model.brand, brand)
                .where(predicates)
                .orderBy(order.date.asc())
                .fetch();
    }

    public List<Order> findOrdersWhereAccidentsSumMoreThanAvgSumOrderByDateQueryDsl() {
        return new JPAQuery<Order>(getEntityManager())
                .select(order)
                .from(order)
                .join(order.accidents, accident)
                .groupBy(order.id)
                .having(accident.damage.avg().gt(
                        new JPAQuery<BigDecimal>(getEntityManager())
                                .select(accident.damage.avg())
                                .from(accident)
                ))
                .orderBy(order.date.asc())
                .fetch();
    }
}