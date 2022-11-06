package com.dmdev.repository;

import com.dmdev.domain.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModelRepository extends JpaRepository<Model, Long>, QuerydslPredicateExecutor<Model> {

    List<Model> findModelsByName(String name);

    @Query(value = "SELECT m " +
            "FROM Model m " +
            "JOIN fetch m.brand b " +
            "WHERE lower(b.name) = lower(:name)")
    List<Model> findModelsByBrandName(@Param("name") String brandName);

    @Query(value = "SELECT m " +
            "FROM Model m " +
            "JOIN fetch m.brand b " +
            "WHERE b.id = :id")
    List<Model> findModelsByBrandId(@Param("id") Long brandId);
}