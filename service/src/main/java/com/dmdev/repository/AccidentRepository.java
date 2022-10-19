package com.dmdev.repository;

import com.dmdev.domain.dto.AccidentFilter;
import com.dmdev.domain.entity.Accident;
import com.dmdev.domain.entity.User;
import com.dmdev.utils.predicate.QPredicate;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QAccident.accident;

public class AccidentRepository extends BaseRepository<Long, Accident> {

    public AccidentRepository(EntityManager entityManager) {
        super(Accident.class, entityManager);
    }

    public List<Accident> findAllQueryDsl() {
        return new JPAQuery<Accident>(getEntityManager())
                .select(accident)
                .from(accident)
                .fetch();
    }

    public Optional<Accident> findByIdQueryDsl(Long id) {
        return Optional.of(new JPAQuery<Accident>(getEntityManager())
                .select(accident)
                .from(accident)
                .where(accident.id.eq(id))
                .fetchOne());
    }

    public List<Accident> findAccidentsByAccidentDateQueryDsl(LocalDate accidentDate) {
        return new JPAQuery<Accident>(getEntityManager())
                .select(accident)
                .from(accident)
                .where(accident.accidentDate.eq(accidentDate))
                .fetch();
    }

    public List<Accident> findAccidentsByCarNumberAndDamageQueryDsl(AccidentFilter accidentFilter) {
        var predicateOr = QPredicate.builder()
                .add(accidentFilter.getDamage(), accident.damage::goe)
                .buildOr();

        var predicateAnd = QPredicate.builder()
                .add(accidentFilter.getCarNumber(), accident.order.car.carNumber::eq)
                .buildAnd();

        var predicateAll = QPredicate.builder()
                .addPredicate(predicateOr)
                .addPredicate(predicateAnd)
                .buildAnd();

        return new JPAQuery<User>(getEntityManager())
                .select(accident)
                .from(accident)
                .where(predicateAll)
                .fetch();
    }

    public List<Accident> findAccidentsByDamageMoreAvgQueryDsl() {
        return new JPAQuery<User>(getEntityManager())
                .select(accident)
                .from(accident)
                .where(accident.damage.gt(new JPAQuery<Double>(getEntityManager())
                        .select(accident.damage.avg())
                        .from(accident)))
                .orderBy(accident.damage.desc())
                .fetch();
    }
}