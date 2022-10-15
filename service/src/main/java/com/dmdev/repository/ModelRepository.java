package com.dmdev.repository;

import com.dmdev.domain.dto.ModelFilter;
import com.dmdev.domain.entity.Brand_;
import com.dmdev.domain.entity.Model;
import com.dmdev.domain.entity.Model_;
import com.dmdev.utils.predicate.CriteriaPredicate;
import com.dmdev.utils.predicate.QPredicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QBrand.brand;
import static com.dmdev.domain.entity.QCategory.category;
import static com.dmdev.domain.entity.QModel.model;

public class ModelRepository implements Repository<Long, Model> {
    private static final ModelRepository INSTANCE = new ModelRepository();

    public static ModelRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Model> findAllHql(Session session) {
        return session.createQuery("select m from Model m", Model.class)
                .list();
    }

    @Override
    public List<Model> findAllCriteria(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Model.class);
        var model = criteria.from(Model.class);

        criteria.select(model);

        return session.createQuery(criteria)
                .list();
    }

    @Override
    public List<Model> findAllQueryDsl(Session session) {
        return new JPAQuery<Model>(session)
                .select(model)
                .from(model)
                .fetch();
    }

    @Override
    public Optional<Model> findByIdCriteria(Session session, Long id) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Model.class);
        var model = criteria.from(Model.class);

        criteria.select(model)
                .where(cb.equal(model.get(Model_.id), id));

        return Optional.ofNullable(session.createQuery(criteria).uniqueResult());
    }

    @Override
    public Optional<Model> findByIdQueryDsl(Session session, Long id) {
        return Optional.ofNullable(new JPAQuery<Model>(session)
                .select(model)
                .from(model)
                .where(model.id.eq(id))
                .fetchOne());
    }

    public List<Model> findModelsByModelAndBrandNameCriteria(Session session, ModelFilter modelFilter) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Model.class);
        var model = criteria.from(Model.class);
        var brand = model.join(Model_.brand);
        Predicate[] predicates = CriteriaPredicate.builder()
                .add(modelFilter.getBrandName(), br -> cb.equal(brand.get(Brand_.name), br))
                .add(modelFilter.getName(), mod -> cb.equal(model.get(Model_.name), mod))
                .getPredicates();

        criteria.select(model)
                .where(predicates);

        return session
                .createQuery(criteria)
                .list();
    }

    public List<Model> findModelsByBrandTransmissionEngineTypeOrderByBrandQueryDsl(Session session, ModelFilter modelFilter) {
        var predicate = QPredicate.builder()
                .add(modelFilter.getBrandName(), brand.name::eq)
                .add(modelFilter.getTransmission(), model.transmission::eq)
                .add(modelFilter.getEngineType(), model.engineType::eq)
                .buildAnd();

        return new JPAQuery<Model>(session)
                .select(model)
                .from(model)
                .join(model.brand, brand)
                .where(predicate)
                .orderBy(brand.name.asc())
                .fetch();
    }

    public List<Model> findModelsByBrandAndCategoryOrderByBrandQueryDsl(Session session, ModelFilter modelFilter) {
        var predicate = QPredicate.builder()
                .add(modelFilter.getBrandName(), brand.name::eq)
                .add(modelFilter.getCategoryName(), category.name::eq)
                .buildOr();

        return new JPAQuery<Model>(session)
                .select(model)
                .from(model)
                .join(model.brand, brand)
                .join(model.category, category)
                .where(predicate)
                .orderBy(brand.name.asc())
                .fetch();
    }
}