package com.dmdev.utils.predicate;

import com.dmdev.domain.dto.filterdto.ModelFilter;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;

import static com.dmdev.domain.entity.QModel.model;

@Component
public class ModelPredicateBuilder implements PredicateBuilder<Predicate, ModelFilter> {

    @Override
    public Predicate build(ModelFilter requestFilter) {
        return QPredicate.builder()
                .add(requestFilter.getBrands(), model.brand.name::in)
                .add(requestFilter.getModels(), model.name::in)
                .add(requestFilter.getCategories(), model.category.name::in)
                .add(requestFilter.getTransmission(), model.transmission::eq)
                .add(requestFilter.getEngineType(), model.engineType::eq)
                .buildAnd();
    }
}