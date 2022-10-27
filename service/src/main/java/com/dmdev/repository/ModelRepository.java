package com.dmdev.repository;

import com.dmdev.domain.dto.ModelFilter;
import com.dmdev.domain.entity.Brand_;
import com.dmdev.domain.entity.Model;
import com.dmdev.domain.entity.Model_;
import com.dmdev.utils.predicate.CriteriaPredicate;
import com.dmdev.utils.predicate.QPredicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QBrand.brand;
import static com.dmdev.domain.entity.QCategory.category;
import static com.dmdev.domain.entity.QModel.model;

@Repository
public class ModelRepository extends BaseRepository<Long, Model> {

    public ModelRepository(EntityManager entityManager) {
        super(Model.class, entityManager);
    }

    public List<Model> findAllQueryDsl() {
        return new JPAQuery<Model>(getEntityManager())
                .select(model)
                .from(model)
                .fetch();
    }

    public Optional<Model> findByIdQueryDsl(Long id) {
        return Optional.of(new JPAQuery<Model>(getEntityManager())
                .select(model)
                .from(model)
                .where(model.id.eq(id))
                .fetchOne());
    }

    public List<Model> findModelsByModelAndBrandNameCriteria(ModelFilter modelFilter) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Model.class);
        var model = criteria.from(Model.class);
        var brand = model.join(Model_.brand);
        Predicate[] predicates = CriteriaPredicate.builder()
                .add(modelFilter.getBrandName(), br -> cb.equal(brand.get(Brand_.name), br))
                .add(modelFilter.getName(), mod -> cb.equal(model.get(Model_.name), mod))
                .getPredicates();

        criteria.select(model)
                .where(predicates);

        return getEntityManager()
                .createQuery(criteria)
                .getResultList();
    }

    public List<Model> findModelsByBrandTransmissionEngineTypeOrderByBrandQueryDsl(ModelFilter modelFilter) {
        var predicate = QPredicate.builder()
                .add(modelFilter.getBrandName(), brand.name::eq)
                .add(modelFilter.getTransmission(), model.transmission::eq)
                .add(modelFilter.getEngineType(), model.engineType::eq)
                .buildAnd();

        return new JPAQuery<Model>(getEntityManager())
                .select(model)
                .from(model)
                .join(model.brand, brand)
                .where(predicate)
                .orderBy(brand.name.asc())
                .fetch();
    }

    public List<Model> findModelsByBrandAndCategoryOrderByBrandQueryDsl(ModelFilter modelFilter) {
        var predicate = QPredicate.builder()
                .add(modelFilter.getBrandName(), brand.name::eq)
                .add(modelFilter.getCategoryName(), category.name::eq)
                .buildOr();

        return new JPAQuery<Model>(getEntityManager())
                .select(model)
                .from(model)
                .join(model.brand, brand)
                .join(model.category, category)
                .where(predicate)
                .orderBy(brand.name.asc())
                .fetch();
    }
}