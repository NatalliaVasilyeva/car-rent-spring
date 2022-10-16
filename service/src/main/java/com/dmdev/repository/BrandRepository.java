package com.dmdev.repository;

import com.dmdev.domain.entity.Brand;
import com.dmdev.domain.entity.Brand_;
import com.querydsl.jpa.impl.JPAQuery;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QBrand.brand;

public class BrandRepository extends BaseRepository<Long, Brand> {

    public BrandRepository(EntityManager entityManager) {
        super(Brand.class, entityManager);
    }

    public List<Brand> findAllHql() {
        return getEntityManager().createQuery("select b from Brand b", Brand.class)
                .getResultList();
    }

    public List<Brand> findAllCriteria() {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Brand.class);
        var brand = criteria.from(Brand.class);

        criteria.select(brand);

        return getEntityManager().createQuery(criteria)
                .getResultList();
    }

    public List<Brand> findAllQueryDsl() {
        return new JPAQuery<Brand>(getEntityManager())
                .select(brand)
                .from(brand)
                .fetch();
    }

    public Optional<Brand> findByIdCriteria(Long id) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Brand.class);
        var brand = criteria.from(Brand.class);

        criteria.select(brand)
                .where(cb.equal(brand.get(Brand_.id), id));

        return Optional.ofNullable(getEntityManager().createQuery(criteria).getSingleResult());
    }

    public Optional<Brand> findByIdQueryDsl(Long id) {
        return Optional.ofNullable(new JPAQuery<Brand>(getEntityManager())
                .select(brand)
                .from(brand)
                .where(brand.id.eq(id))
                .fetchOne());
    }

    public Optional<Brand> findBrandByNameCriteria(String name) {
        var cb = getEntityManager().getCriteriaBuilder();
        var criteria = cb.createQuery(Brand.class);
        var brand = criteria.from(Brand.class);

        criteria.select(brand)
                .where(cb.equal(brand.get(Brand_.name), name));

        return Optional.ofNullable(getEntityManager().createQuery(criteria).getSingleResult());
    }

    public Optional<Brand> findBrandByNameQueryDsl(String name) {
        return Optional.ofNullable(new JPAQuery<Brand>(getEntityManager())
                .select(brand)
                .from(brand)
                .where(brand.name.eq(name))
                .fetchOne());
    }
}