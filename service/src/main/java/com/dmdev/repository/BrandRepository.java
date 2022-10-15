package com.dmdev.repository;

import com.dmdev.domain.entity.Brand;
import com.dmdev.domain.entity.Brand_;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QBrand.brand;

public class BrandRepository implements Repository<Long, Brand> {
    private static final BrandRepository INSTANCE = new BrandRepository();

    public static BrandRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Brand> findAllHql(Session session) {
        return session.createQuery("select b from Brand b", Brand.class)
                .list();
    }

    @Override
    public List<Brand> findAllCriteria(Session session) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Brand.class);
        var brand = criteria.from(Brand.class);

        criteria.select(brand);

        return session.createQuery(criteria)
                .list();
    }

    @Override
    public List<Brand> findAllQueryDsl(Session session) {
        return new JPAQuery<Brand>(session)
                .select(brand)
                .from(brand)
                .fetch();
    }

    @Override
    public Optional<Brand> findByIdCriteria(Session session, Long id) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Brand.class);
        var brand = criteria.from(Brand.class);

        criteria.select(brand)
                .where(cb.equal(brand.get(Brand_.id), id));

        return Optional.ofNullable(session.createQuery(criteria).uniqueResult());
    }

    @Override
    public Optional<Brand> findByIdQueryDsl(Session session, Long id) {
        return Optional.ofNullable(new JPAQuery<Brand>(session)
                .select(brand)
                .from(brand)
                .where(brand.id.eq(id))
                .fetchOne());
    }

    public Optional<Brand> findBrandByNameCriteria(Session session, String name) {
        var cb = session.getCriteriaBuilder();
        var criteria = cb.createQuery(Brand.class);
        var brand = criteria.from(Brand.class);

        criteria.select(brand)
                .where(cb.equal(brand.get(Brand_.name), name));

        return Optional.ofNullable(session.createQuery(criteria).uniqueResult());
    }

    public Optional<Brand> findBrandByNameQueryDsl(Session session, String name) {
        return Optional.ofNullable(new JPAQuery<Brand>(session)
                .select(brand)
                .from(brand)
                .where(brand.name.eq(name))
                .fetchOne());
    }
}