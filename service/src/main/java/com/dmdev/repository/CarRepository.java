package com.dmdev.repository;

import com.dmdev.domain.dto.CarFilter;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.domain.entity.Car_;
import com.dmdev.utils.predicate.CriteriaPredicate;
import com.dmdev.utils.predicate.QPredicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QCar.car;
import static com.dmdev.domain.entity.QModel.model;

public class CarRepository implements Repository<Long, Car> {
    private static final CarRepository INSTANCE = new CarRepository();

    public static CarRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Car> findAllHQL(Session session) {
        return session.createQuery("select c from Car c", Car.class)
                .list();
    }

    @Override
    public List<Car> findAllCriteria(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Car.class);
        var car = criteria.from(Car.class);

        criteria.select(car);

        return session.createQuery(criteria)
                .list();
    }

    @Override
    public List<Car> findAllQueryDsl(Session session) {
        return new JPAQuery<Car>(session)
                .select(car)
                .from(car)
                .fetch();
    }

    @Override
    public Optional<Car> findByIdCriteria(Session session, Long id) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Car.class);
        var car = criteria.from(Car.class);

        criteria.select(car)
                .where(cb.equal(car.get(Car_.id), id));

        return Optional.ofNullable(session.createQuery(criteria).uniqueResult());
    }

    @Override
    public Optional<Car> findByIdQueryDsl(Session session, Long id) {
        return Optional.ofNullable(new JPAQuery<Car>(session)
                .select(car)
                .from(car)
                .where(car.id.eq(id))
                .fetchOne());
    }

    public Optional<Car> findCarByNumberCriteria(Session session, String carNumber) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Car.class);
        var car = criteria.from(Car.class);

        criteria.select(car)
                .where(cb.equal(car.get(Car_.carNumber), carNumber));

        return Optional.ofNullable(session.createQuery(criteria).uniqueResult());
    }

    public List<Car> findCarsByColorAndYearOrGreaterCriteria(Session session, CarFilter carFilter) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Car.class);
        var car = criteria.from(Car.class);
        Predicate[] predicates = CriteriaPredicate.builder()
                .add(carFilter.getYear(), year -> cb.greaterThanOrEqualTo(car.get(Car_.year), year))
                .add(carFilter.getColor(), color -> cb.equal(car.get(Car_.color.getName()), color))
                .getPredicates();

        criteria.select(car)
                .where(predicates);

        return session.createQuery(criteria)
                .list();
    }

    public List<Car> findCarsByColorAndYearOrGreaterQueryDsl(Session session, CarFilter carFilter) {
        var predicateYear = QPredicate.builder()
                .add(carFilter.getYear(), car.year::eq)
                .add(carFilter.getYear(), car.year::gt)
                .buildOr();

        var predicateColor = QPredicate.builder()
                .add(carFilter.getColor(), car.color::eq)
                .buildAnd();

        var predicateAll = QPredicate.builder()
                .addPredicate(predicateYear)
                .addPredicate(predicateColor)
                .buildAnd();

        return new JPAQuery<Car>(session)
                .select(car)
                .from(car)
                .where(predicateAll)
                .fetch();
    }

    public List<Car> findCarsByBrandModelCategoryYearOrGreaterQueryDsl(Session session, CarFilter carFilter) {
        var predicateYear = QPredicate.builder()
                .add(carFilter.getYear(), car.year::eq)
                .add(carFilter.getYear(), car.year::gt)
                .buildOr();

        var predicateOther = QPredicate.builder()
                .add(carFilter.getBrandName(), car.model.brand.name::eq)
                .add(carFilter.getModelName(), car.model.name::eq)
                .add(carFilter.getCategory(), car.model.category.name::eq)
                .buildAnd();

        var predicateAll = QPredicate.builder()
                .addPredicate(predicateYear)
                .addPredicate(predicateOther)
                .buildAnd();

        return new JPAQuery<CarRentalTime>(session)
                .select(car)
                .from(car)
                .join(car.model, model)
                .join(model.brand)
                .join(model.category)
                .where(predicateAll)
                .fetch();
    }
}