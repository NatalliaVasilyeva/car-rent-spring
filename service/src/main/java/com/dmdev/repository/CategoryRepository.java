package com.dmdev.repository;

import com.dmdev.domain.entity.Category;
import com.dmdev.domain.entity.Category_;
import com.dmdev.utils.predicate.QPredicate;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QCategory.category;

public class CategoryRepository extends BaseRepository<Long, Category> {

    public CategoryRepository(EntityManager entityManager) {
        super(Category.class, entityManager);
    }

    public List<Category> findAllHql() {
        return getEntityManager().createQuery("select c from Category c", Category.class)
                .getResultList();
    }

    public List<Category> findAllCriteria() {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Category.class);
        var category = criteria.from(Category.class);

        criteria.select(category);

        return getEntityManager().createQuery(criteria)
                .getResultList();
    }

    public List<Category> findAllQueryDsl() {
        return new JPAQuery<Category>(getEntityManager())
                .select(category)
                .from(category)
                .fetch();
    }

    public Optional<Category> findByIdCriteria(Long id) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Category.class);
        var category = criteria.from(Category.class);

        criteria.select(category)
                .where(cb.equal(category.get(Category_.id), id));

        return Optional.ofNullable(getEntityManager().createQuery(criteria).getSingleResult());
    }

    public Optional<Category> findByIdQueryDsl(Long id) {
        return Optional.ofNullable(new JPAQuery<Category>(getEntityManager())
                .select(category)
                .from(category)
                .where(category.id.eq(id))
                .fetchOne());
    }

    public List<Category> findCategoriesByPriceCriteria(BigDecimal price) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Category.class);
        var category = criteria.from(Category.class);

        criteria.select(category)
                .where(cb.equal(category.get(Category_.price), price));

        return getEntityManager().createQuery(criteria)
                .getResultList();
    }

    public List<Category> findCategoriesByPriceQueryDsl(BigDecimal price) {
        return new JPAQuery<Category>(getEntityManager())
                .select(category)
                .from(category)
                .where(category.price.eq(price))
                .fetch();
    }

    public List<Category> findCategoriesByPriceLessThanQueryDsl(BigDecimal price) {
        var predicateOr = QPredicate.builder()
                .add(price, category.price::loe)
                .buildOr();

        return new JPAQuery<Category>(getEntityManager())
                .select(category)
                .from(category)
                .where(predicateOr)
                .fetch();
    }

    public List<Category> findCategoriesByNameCriteria(String name) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Category.class);
        var category = criteria.from(Category.class);

        criteria.select(category)
                .where(cb.equal(category.get(Category_.name), name));

        return getEntityManager().createQuery(criteria)
                .getResultList();
    }
}