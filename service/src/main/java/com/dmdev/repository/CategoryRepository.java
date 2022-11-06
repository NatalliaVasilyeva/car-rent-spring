package com.dmdev.repository;

import com.dmdev.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long>, QuerydslPredicateExecutor<Category> {

    Optional<Category> findByNameIgnoringCase(String name);

    List<Category> findAllByPrice(BigDecimal price);

    List<Category> findAllByPriceLessThanEqual(BigDecimal price);

    List<Category> findAllByPriceGreaterThanEqual(BigDecimal price);
}