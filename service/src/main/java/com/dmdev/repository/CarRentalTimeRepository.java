package com.dmdev.repository;

import com.dmdev.domain.dto.CarRentalTimeFilter;
import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.domain.entity.CarRentalTime_;
import com.dmdev.domain.entity.Order_;
import com.dmdev.utils.predicate.CriteriaPredicate;
import com.dmdev.utils.predicate.QPredicate;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QCarRentalTime.carRentalTime;
import static com.dmdev.domain.entity.QOrder.order;

public class CarRentalTimeRepository extends BaseRepository<Long, CarRentalTime> {

    public CarRentalTimeRepository(EntityManager entityManager) {
        super(CarRentalTime.class, entityManager);
    }

    public List<CarRentalTime> findAllHql() {
        return getEntityManager().createQuery("select c from CarRentalTime c", CarRentalTime.class)
                .getResultList();
    }

    public List<CarRentalTime> findAllCriteria() {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(CarRentalTime.class);
        var carRentalTime = criteria.from(CarRentalTime.class);

        criteria.select(carRentalTime);

        return getEntityManager().createQuery(criteria)
                .getResultList();
    }

    public List<CarRentalTime> findAllQueryDsl() {
        return new JPAQuery<CarRentalTime>(getEntityManager())
                .select(carRentalTime)
                .from(carRentalTime)
                .fetch();
    }

    public Optional<CarRentalTime> findByIdCriteria(Long id) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(CarRentalTime.class);
        var carRentalTime = criteria.from(CarRentalTime.class);

        criteria.select(carRentalTime)
                .where(cb.equal(carRentalTime.get(CarRentalTime_.id), id));

        return Optional.ofNullable(getEntityManager().createQuery(criteria).getSingleResult());
    }

    public Optional<CarRentalTime> findByIdQueryDsl(Long id) {
        return Optional.ofNullable(new JPAQuery<CarRentalTime>(getEntityManager())
                .select(carRentalTime)
                .from(carRentalTime)
                .where(carRentalTime.id.eq(id))
                .fetchOne());
    }

    public Optional<CarRentalTime> findCarRentalTimesByOrderIdCriteria(Long orderId) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(CarRentalTime.class);
        var carRentalTime = criteria.from(CarRentalTime.class);
        var order = carRentalTime.join(CarRentalTime_.order);

        criteria.select(carRentalTime)
                .where(cb.equal(order.get(Order_.id), orderId));

        return Optional.ofNullable(getEntityManager().createQuery(criteria).getSingleResult());
    }

    public Optional<CarRentalTime> findCarRentalTimesByOrderIdQueryDsl(Long orderId) {
        return Optional.ofNullable(new JPAQuery<CarRentalTime>(getEntityManager())
                .select(carRentalTime)
                .from(carRentalTime)
                .join(carRentalTime.order, order)
                .where(order.id.eq(orderId))
                .fetchOne()
        );
    }

    public List<CarRentalTime> findCarRentalTimesBetweenStartAndRentalDatesCriteria(CarRentalTimeFilter carRentalTimeFilter) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(CarRentalTime.class);
        var carRentalTime = criteria.from(CarRentalTime.class);
        Predicate[] predicates = CriteriaPredicate.builder()
                .add(carRentalTimeFilter.getStartRentalDate(), startDate -> cb.greaterThanOrEqualTo(carRentalTime.get(CarRentalTime_.startRentalDate), startDate))
                .add(carRentalTimeFilter.getEndRentalDate(), endDate -> cb.lessThanOrEqualTo(carRentalTime.get(CarRentalTime_.endRentalDate), endDate))
                .getPredicates();

        criteria.select(carRentalTime)
                .where(predicates);

        return getEntityManager().createQuery(criteria)
                .getResultList();
    }

    public List<CarRentalTime> findCarRentalTimesBetweenStartAndRentalDatesQueryDsl(CarRentalTimeFilter carRentalTimeFilter) {
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

        return new JPAQuery<CarRentalTime>(getEntityManager())
                .select(carRentalTime)
                .from(carRentalTime)
                .where(predicateAll)
                .fetch();
    }
}