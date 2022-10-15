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
import org.hibernate.Session;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QAccident.accident;

public class AccidentRepository implements Repository<Long, Accident> {
    private static final AccidentRepository INSTANCE = new AccidentRepository();

    public static AccidentRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Accident> findAllHQL(Session session) {
        return session.createQuery("select a from Accident a", Accident.class)
                .list();
    }

    @Override
    public List<Accident> findAllCriteria(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Accident.class);
        var accident = criteria.from(Accident.class);

        criteria.select(accident);

        return session.createQuery(criteria)
                .list();
    }

    @Override
    public List<Accident> findAllQueryDsl(Session session) {
        return new JPAQuery<Accident>(session)
                .select(accident)
                .from(accident)
                .fetch();
    }

    @Override
    public Optional<Accident> findByIdCriteria(Session session, Long id) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Accident.class);
        var accident = criteria.from(Accident.class);

        criteria.select(accident)
                .where(cb.equal(accident.get(Accident_.id), id));

        return Optional.ofNullable(session.createQuery(criteria).uniqueResult());
    }

    @Override
    public Optional<Accident> findByIdQueryDsl(Session session, Long id) {
        return Optional.ofNullable(new JPAQuery<Accident>(session)
                .select(accident)
                .from(accident)
                .where(accident.id.eq(id))
                .fetchOne());
    }

    public List<Accident> findAccidentsByAccidentDateCriteria(Session session, LocalDate accidentDate) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Accident.class);
        var accident = criteria.from(Accident.class);

        criteria.select(accident)
                .where(cb.equal(accident.get(Accident_.accidentDate), accidentDate));

        return session.createQuery(criteria).list();
    }

    public List<Accident> findAccidentsByAccidentDateQueryDsl(Session session, LocalDate accidentDate) {

        return new JPAQuery<Accident>(session)
                .select(accident)
                .from(accident)
                .where(accident.accidentDate.eq(accidentDate))
                .fetch();
    }

    public List<Accident> findAccidentsByCarNumberAndDamageCriteria(Session session, AccidentFilter accidentFilter) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Accident.class);
        var accident = criteria.from(Accident.class);
        Predicate[] predicates = CriteriaPredicate.builder()
                .add(accidentFilter.getDamage(), damage -> cb.greaterThanOrEqualTo(accident.get(Accident_.damage), damage))
                .add(accidentFilter.getCarNumber(), carNumber -> cb.equal(accident.get(Accident_.order).get(Order_.car).get(Car_.carNumber), carNumber))
                .getPredicates();

        criteria.select(accident)
                .where(predicates);

        return session.createQuery(criteria)
                .list();
    }

    public List<Accident> findAccidentsByCarNumberAndDamageQueryDsl(Session session, AccidentFilter accidentFilter) {
        var predicateOr = QPredicate.builder()
                .add(accidentFilter.getDamage(), accident.damage::eq)
                .add(accidentFilter.getDamage(), accident.damage::gt)
                .buildOr();

        var predicateAnd = QPredicate.builder()
                .add(accidentFilter.getCarNumber(), accident.order.car.carNumber::eq)
                .buildAnd();

        var predicateAll = QPredicate.builder()
                .addPredicate(predicateOr)
                .addPredicate(predicateAnd)
                .buildAnd();

        return new JPAQuery<User>(session)
                .select(accident)
                .from(accident)
                .where(predicateAll)
                .fetch();
    }

    public List<Accident> findAccidentsByDamageMoreAvgCriteria(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Accident.class);
        var accident = criteria.from(Accident.class);
        var subquery = criteria.subquery(Double.class);
        var subqueryDamage = subquery.from(Accident.class);

        criteria.select(accident)
                .where(cb.gt(accident.get(Accident_.damage),
                        subquery.select(cb.avg(subqueryDamage.get(Accident_.damage)))))
                .orderBy(cb.desc(accident.get(Accident_.damage)));

        return session.createQuery(criteria).list();
    }

    public List<Accident> findAccidentsByDamageMoreAvgQueryDsl(Session session) {
        return new JPAQuery<User>(session)
                .select(accident)
                .from(accident)
                .where(accident.damage.gt(new JPAQuery<Double>(session)
                        .select(accident.damage.avg())
                        .from(accident)))
                .orderBy(accident.damage.desc())
                .fetch();
    }
}