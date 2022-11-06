package com.dmdev.repository;

import com.dmdev.domain.dto.CarFilter;
import com.dmdev.domain.entity.Car;
import com.dmdev.domain.entity.CarRentalTime;
import com.dmdev.domain.entity.Car_;
import com.dmdev.domain.model.Transmission;
import com.dmdev.utils.predicate.QPredicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.graph.GraphSemantic;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QCar.car;
import static com.dmdev.domain.entity.QModel.model;

@Repository
public class CarRepository extends BaseRepository<Long, Car> {

    public CarRepository() {
        super(Car.class);
    }

    public List<Car> findAllQueryDsl() {
        return new JPAQuery<Car>(getEntityManager())
                .select(car)
                .from(car)
                .fetch();
    }

    public Optional<Car> findByIdQueryDsl(Long id) {
        return Optional.ofNullable(new JPAQuery<Car>(getEntityManager())
                .select(car)
                .from(car)
                .where(car.id.eq(id))
                .fetchOne());
    }

    public Optional<Car> findCarByNumberCriteria(String carNumber) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Car.class);
        var car = criteria.from(Car.class);

        criteria.select(car)
                .where(cb.equal(car.get(Car_.carNumber), carNumber));

        return Optional.ofNullable(getEntityManager().createQuery(criteria).getSingleResult());
    }

    public List<Car> findCarByTransmissionGraph(Transmission transmission) {
        var carGraph = getEntityManager().createEntityGraph(Car.class);
        carGraph.addAttributeNodes("model");

        return new JPAQuery<Car>(getEntityManager())
                .select(car)
                .setHint(GraphSemantic.LOAD.getJpaHintName(), carGraph)
                .from(car)
                .where(car.model.transmission.eq(transmission))
                .fetch();
    }

    public List<Car> findCarsByColorAndYearOrGreaterQueryDsl(CarFilter carFilter) {
        var predicateYear = QPredicate.builder()
                .add(carFilter.getYear(), car.year::goe)
                .buildOr();

        var predicateColor = QPredicate.builder()
                .add(carFilter.getColor(), car.color::eq)
                .buildAnd();

        var predicateAll = QPredicate.builder()
                .addPredicate(predicateYear)
                .addPredicate(predicateColor)
                .buildAnd();

        return new JPAQuery<Car>(getEntityManager())
                .select(car)
                .from(car)
                .where(predicateAll)
                .fetch();
    }

    public List<Car> findCarsByBrandModelCategoryYearOrGreaterQueryDsl(CarFilter carFilter) {
        var predicateYear = QPredicate.builder()
                .add(carFilter.getYear(), car.year::goe)
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

        return new JPAQuery<CarRentalTime>(getEntityManager())
                .select(car)
                .from(car)
                .join(car.model, model)
                .join(model.brand)
                .join(model.category)
                .where(predicateAll)
                .fetch();
    }
}