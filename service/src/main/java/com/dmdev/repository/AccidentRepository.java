package com.dmdev.repository;

import com.dmdev.domain.dto.AccidentFilter;
import com.dmdev.domain.entity.Accident;
import com.dmdev.domain.entity.User;
import com.dmdev.utils.predicate.QPredicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QAccident.accident;

@Repository
public class AccidentRepository extends BaseRepository<Long, Accident> {

    public AccidentRepository() {
        super(Accident.class);
    }

    public List<Accident> findAllQueryDsl() {
        return new JPAQuery<Accident>(getEntityManager())
                .select(accident)
                .from(accident)
                .fetch();
    }

    public Optional<Accident> findByIdQueryDsl(Long id) {
        return Optional.ofNullable(new JPAQuery<Accident>(getEntityManager())
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