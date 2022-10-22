package com.dmdev.repository;

import com.dmdev.domain.entity.Brand;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.dmdev.domain.entity.QBrand.brand;

@Repository
public class BrandRepository extends BaseRepository<Long, Brand> {

    public BrandRepository(EntityManager entityManager) {
        super(Brand.class, entityManager);
    }

    public List<Brand> findAllQueryDsl() {
        return new JPAQuery<Brand>(getEntityManager())
                .select(brand)
                .from(brand)
                .fetch();
    }

    public Optional<Brand> findByIdQueryDsl(Long id) {
        return Optional.of(new JPAQuery<Brand>(getEntityManager())
                .select(brand)
                .from(brand)
                .where(brand.id.eq(id))
                .fetchOne());
    }

    public Optional<Brand> findBrandByNameQueryDsl(String name) {
        return Optional.of(new JPAQuery<Brand>(getEntityManager())
                .select(brand)
                .from(brand)
                .where(brand.name.eq(name))
                .fetchOne());
    }
}