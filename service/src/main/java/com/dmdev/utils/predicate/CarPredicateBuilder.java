package com.dmdev.utils.predicate;

import com.dmdev.domain.dto.filterdto.CarFilter;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;

import static com.dmdev.domain.entity.QCar.car;

@Component
public class CarPredicateBuilder implements PredicateBuilder<Predicate, CarFilter> {

    @Override
    public Predicate build(CarFilter requestFilter) {
        return QPredicate.builder()
                .add(requestFilter.getColor(), car.color::eq)
                .add(requestFilter.getYear(), car.year::goe)
                .add(requestFilter.getModelNames(), car.model.name::in)
                .add(requestFilter.getBrandNames(), car.model.brand.name::in)
                .add(requestFilter.getCategoryName(), car.model.category.name::eq)
                .add(requestFilter.getTransmission(), car.model.transmission::eq)
                .add(requestFilter.getEngineType(), car.model.engineType::eq)
                .buildAnd();
    }
}