package com.dmdev.repository;

import com.dmdev.domain.entity.Brand;
import com.dmdev.domain.projection.BrandFullView;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long>, QuerydslPredicateExecutor<Brand> {

    Optional<Brand> findByNameIgnoringCase(String name);

    @EntityGraph(attributePaths = {"models"})
    Optional<Brand> findByName(String name);

    @EntityGraph(attributePaths = {"models"})
    List<Brand> findByNameIn(List<String> names);

    @Query(value = "SELECT b " +
            "FROM Brand b " +
            "JOIN fetch b.models")
    List<BrandFullView> findAllFull();

    @Query(value = "SELECT b " +
            "FROM Brand b " +
            "JOIN fetch b.models " +
            "WHERE b.id = :id")
    Optional<BrandFullView> findByIdAllFull(@Param("id") Long brandId);

    @Query(value = "SELECT b " +
            "FROM Brand b " +
            "JOIN fetch b.models " +
            "WHERE lower(b.name) = lower(:name)")
    List<BrandFullView> findByNameAllFull(@Param("name") String name);

    @Query(value = "SELECT b " +
            "FROM Brand b " +
            "JOIN fetch b.models " +
            "WHERE b.name IN :names")
    List<BrandFullView> findByNameAllFullInIgnoringCase(@Param("names") List<String> names);
}