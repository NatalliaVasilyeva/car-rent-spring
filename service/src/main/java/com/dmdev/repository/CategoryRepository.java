package com.dmdev.repository;

import com.dmdev.domain.entity.Category;
import com.dmdev.domain.entity.Category_;
import com.dmdev.utils.predicate.QPredicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QCategory.category;

public class CategoryRepository implements Repository<Long, Category> {
    private static final CategoryRepository INSTANCE = new CategoryRepository();

    public static CategoryRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Category> findAllHQL(Session session) {

        return session.createQuery("select c from Category c", Category.class)
                .list();
    }

    @Override
    public List<Category> findAllCriteria(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Category.class);
        var category = criteria.from(Category.class);

        criteria.select(category);

        return session.createQuery(criteria)
                .list();
    }

    @Override
    public List<Category> findAllQueryDsl(Session session) {
        return new JPAQuery<Category>(session)
                .select(category)
                .from(category)
                .fetch();
    }

    @Override
    public Optional<Category> findByIdCriteria(Session session, Long id) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Category.class);
        var category = criteria.from(Category.class);

        criteria.select(category)
                .where(cb.equal(category.get(Category_.id), id));

        return Optional.ofNullable(session.createQuery(criteria).uniqueResult());
    }

    @Override
    public Optional<Category> findByIdQueryDsl(Session session, Long id) {
        return Optional.ofNullable(new JPAQuery<Category>(session)
                .select(category)
                .from(category)
                .where(category.id.eq(id))
                .fetchOne());
    }

    public List<Category> findCategoriesByPriceCriteria(Session session, BigDecimal price) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Category.class);
        var category = criteria.from(Category.class);

        criteria.select(category)
                .where(cb.equal(category.get(Category_.price), price));

        return session.createQuery(criteria)
                .list();
    }

    public List<Category> findCategoriesByPriceQueryDsl(Session session, BigDecimal price) {
        return new JPAQuery<Category>(session)
                .select(category)
                .from(category)
                .where(category.price.eq(price))
                .fetch();
    }

    public List<Category> findCategoriesByPriceLessThanQueryDsl(Session session, BigDecimal price) {
        var predicateOr = QPredicate.builder()
                .add(price, category.price::eq)
                .add(price, category.price::lt)
                .buildOr();

        return new JPAQuery<Category>(session)
                .select(category)
                .from(category)
                .where(predicateOr)
                .fetch();
    }

    public List<Category> findCategoriesByNameCriteria(Session session, String name) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Category.class);
        var category = criteria.from(Category.class);

        criteria.select(category)
                .where(cb.equal(category.get(Category_.name), name));

        return session.createQuery(criteria)
                .list();
    }
}