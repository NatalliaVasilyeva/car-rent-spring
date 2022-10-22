package com.dmdev.repository;

import com.dmdev.domain.dto.CarRentalTimeFilter;
import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.utils.predicate.QPredicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QCarRentalTime.carRentalTime;
import static com.dmdev.domain.entity.QOrder.order;

@Repository
public class CarRentalTimeRepository extends BaseRepository<Long, CarRentalTime> {

    public CarRentalTimeRepository(EntityManager entityManager) {
        super(CarRentalTime.class, entityManager);
    }

    public List<CarRentalTime> findAllQueryDsl() {
        return new JPAQuery<CarRentalTime>(getEntityManager())
                .select(carRentalTime)
                .from(carRentalTime)
                .fetch();
    }

    public Optional<CarRentalTime> findByIdQueryDsl(Long id) {
        return Optional.of(new JPAQuery<CarRentalTime>(getEntityManager())
                .select(carRentalTime)
                .from(carRentalTime)
                .where(carRentalTime.id.eq(id))
                .fetchOne());
    }

    public Optional<CarRentalTime> findCarRentalTimesByOrderIdQueryDsl(Long orderId) {
        return Optional.of(new JPAQuery<CarRentalTime>(getEntityManager())
                .select(carRentalTime)
                .from(carRentalTime)
                .join(carRentalTime.order, order)
                .where(order.id.eq(orderId))
                .fetchOne()
        );
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