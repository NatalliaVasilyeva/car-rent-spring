package com.dmdev.repository;

import com.dmdev.domain.dto.AccidentFilter;
import com.dmdev.domain.entity.Accident;
import com.dmdev.domain.entity.Accident_;
import com.dmdev.domain.entity.Car_;
import com.dmdev.domain.entity.Order_;
import com.dmdev.domain.entity.User;
import com.dmdev.utils.predicate.CriteriaPredicate;
import com.dmdev.utils.predicate.QPredicate;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QAccident.accident;

public class AccidentRepository extends BaseRepository<Long, Accident> {

    public AccidentRepository(EntityManager entityManager) {
        super(Accident.class, entityManager);
    }

    public List<Accident> findAllHql() {
        return getEntityManager().createQuery("select a from Accident a", Accident.class)
                .getResultList();
    }

    public List<Accident> findAllCriteria() {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Accident.class);
        var accident = criteria.from(Accident.class);

        criteria.select(accident);

        return getEntityManager().createQuery(criteria)
                .getResultList();
    }

    public List<Accident> findAllQueryDsl() {
        return new JPAQuery<Accident>(getEntityManager())
                .select(accident)
                .from(accident)
                .fetch();
    }

    public Optional<Accident> findByIdCriteria(Long id) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Accident.class);
        var accident = criteria.from(Accident.class);

        criteria.select(accident)
                .where(cb.equal(accident.get(Accident_.id), id));

        return Optional.ofNullable(getEntityManager().createQuery(criteria).getSingleResult());
    }

    public Optional<Accident> findByIdQueryDsl(Long id) {
        return Optional.ofNullable(new JPAQuery<Accident>(getEntityManager())
                .select(accident)
                .from(accident)
                .where(accident.id.eq(id))
                .fetchOne());
    }

    public List<Accident> findAccidentsByAccidentDateCriteria(LocalDate accidentDate) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Accident.class);
        var accident = criteria.from(Accident.class);

        criteria.select(accident)
                .where(cb.equal(accident.get(Accident_.accidentDate), accidentDate));

        return getEntityManager().createQuery(criteria).getResultList();
    }

    public List<Accident> findAccidentsByAccidentDateQueryDsl(LocalDate accidentDate) {
        return new JPAQuery<Accident>(getEntityManager())
                .select(accident)
                .from(accident)
                .where(accident.accidentDate.eq(accidentDate))
                .fetch();
    }

    public List<Accident> findAccidentsByCarNumberAndDamageCriteria(AccidentFilter accidentFilter) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Accident.class);
        var accident = criteria.from(Accident.class);
        Predicate[] predicates = CriteriaPredicate.builder()
                .add(accidentFilter.getDamage(), damage -> cb.greaterThanOrEqualTo(accident.get(Accident_.damage), damage))
                .add(accidentFilter.getCarNumber(), carNumber -> cb.equal(accident.get(Accident_.order).get(Order_.car).get(Car_.carNumber), carNumber))
                .getPredicates();

        criteria.select(accident)
                .where(predicates);

        return getEntityManager().createQuery(criteria)
                .getResultList();
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

    public List<Accident> findAccidentsByDamageMoreAvgCriteria() {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Accident.class);
        var accident = criteria.from(Accident.class);
        var subquery = criteria.subquery(Double.class);
        var subqueryDamage = subquery.from(Accident.class);

        criteria.select(accident)
                .where(cb.gt(accident.get(Accident_.damage),
                        subquery.select(cb.avg(subqueryDamage.get(Accident_.damage)))))
                .orderBy(cb.desc(accident.get(Accident_.damage)));

        return getEntityManager().createQuery(criteria).getResultList();
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