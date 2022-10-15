package com.dmdev.repository;

import com.dmdev.domain.dto.CarRentalTimeFilter;
import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.domain.entity.CarRentalTime_;
import com.dmdev.domain.entity.Order_;
import com.dmdev.utils.predicate.CriteriaPredicate;
import com.dmdev.utils.predicate.QPredicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QCarRentalTime.carRentalTime;
import static com.dmdev.domain.entity.QOrder.order;

public class CarRentalTimeRepository implements Repository<Long, CarRentalTime> {
    private static final CarRentalTimeRepository INSTANCE = new CarRentalTimeRepository();

    public static CarRentalTimeRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<CarRentalTime> findAllHql(Session session) {
        return session.createQuery("select c from CarRentalTime c", CarRentalTime.class)
                .list();
    }

    @Override
    public List<CarRentalTime> findAllCriteria(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(CarRentalTime.class);
        var carRentalTime = criteria.from(CarRentalTime.class);

        criteria.select(carRentalTime);

        return session.createQuery(criteria)
                .list();
    }

    @Override
    public List<CarRentalTime> findAllQueryDsl(Session session) {
        return new JPAQuery<CarRentalTime>(session)
                .select(carRentalTime)
                .from(carRentalTime)
                .fetch();
    }

    @Override
    public Optional<CarRentalTime> findByIdCriteria(Session session, Long id) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(CarRentalTime.class);
        var carRentalTime = criteria.from(CarRentalTime.class);

        criteria.select(carRentalTime)
                .where(cb.equal(carRentalTime.get(CarRentalTime_.id), id));

        return Optional.ofNullable(session.createQuery(criteria).uniqueResult());
    }

    @Override
    public Optional<CarRentalTime> findByIdQueryDsl(Session session, Long id) {
        return Optional.ofNullable(new JPAQuery<CarRentalTime>(session)
                .select(carRentalTime)
                .from(carRentalTime)
                .where(carRentalTime.id.eq(id))
                .fetchOne());
    }

    public Optional<CarRentalTime> findCarRentalTimesByOrderIdCriteria(Session session, Long orderId) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(CarRentalTime.class);
        var carRentalTime = criteria.from(CarRentalTime.class);
        var order = carRentalTime.join(CarRentalTime_.order);

        criteria.select(carRentalTime)
                .where(cb.equal(order.get(Order_.id), orderId));

        return session.createQuery(criteria).uniqueResultOptional();
    }

    public Optional<CarRentalTime> findCarRentalTimesByOrderIdQueryDsl(Session session, Long orderId) {
        return Optional.ofNullable(new JPAQuery<CarRentalTime>(session)
                .select(carRentalTime)
                .from(carRentalTime)
                .join(carRentalTime.order, order)
                .where(order.id.eq(orderId))
                .fetchOne()
        );
    }

    public List<CarRentalTime> findCarRentalTimesBetweenStartAndRentalDatesCriteria(Session session, CarRentalTimeFilter carRentalTimeFilter) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(CarRentalTime.class);
        var carRentalTime = criteria.from(CarRentalTime.class);
        Predicate[] predicates = CriteriaPredicate.builder()
                .add(carRentalTimeFilter.getStartRentalDate(), startDate -> cb.greaterThanOrEqualTo(carRentalTime.get(CarRentalTime_.startRentalDate), startDate))
                .add(carRentalTimeFilter.getEndRentalDate(), endDate -> cb.lessThanOrEqualTo(carRentalTime.get(CarRentalTime_.endRentalDate), endDate))
                .getPredicates();

        criteria.select(carRentalTime)
                .where(predicates);

        return session.createQuery(criteria)
                .list();
    }

    public List<CarRentalTime> findCarRentalTimesBetweenStartAndRentalDatesQueryDsl(Session session, CarRentalTimeFilter carRentalTimeFilter) {
        var predicateOrStart = QPredicate.builder()
                .add(carRentalTimeFilter.getStartRentalDate(), carRentalTime.startRentalDate::goe)
                .buildOr();

        var predicateOrEnd = QPredicate.builder()
                .add(carRentalTimeFilter.getEndRentalDate(), carRentalTime.startRentalDate::loe)
                .buildOr();

        var predicateAll = QPredicate.builder()
                .addPredicate(predicateOrStart)
                .addPredicate(predicateOrEnd)
                .buildAnd();

        return new JPAQuery<CarRentalTime>(session)
                .select(carRentalTime)
                .from(carRentalTime)
                .where(predicateAll)
                .fetch();
    }
}