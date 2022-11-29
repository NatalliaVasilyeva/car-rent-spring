package com.dmdev.repository;

import com.dmdev.domain.entity.Car;
import com.dmdev.domain.model.Transmission;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long>, QuerydslPredicateExecutor<Car> {

    Optional<Car> findByCarNumber(String carNumber);

    List<Car> findByCarNumberContainingIgnoreCase(String carNumber);

    @EntityGraph(attributePaths = {"model"})
    @Query(value = "SELECT c " +
            "FROM Car c " +
            "JOIN fetch c.model m " +
            "WHERE m.transmission = :transmission ")
    List<Car> findByTransmissionIgnoreCase(@Param("transmission") Transmission transmission);

    @Query(value = "SELECT c " +
            "FROM Car c " +
            "LEFT JOIN fetch c.orders o " +
            "WHERE o.accidents.size > 0 ")
    List<Car> findAllWithAccidents();

    @Query(value = "SELECT c " +
            "FROM Car c " +
            "LEFT JOIN fetch c.orders o " +
            "WHERE o.accidents.size = 0 ")
    List<Car> findAllWithoutAccidents();

    @Query(value = "SELECT c " +
            "FROM Car c " +
            "WHERE c.repaired = false")
    List<Car> findAllAvailable();

    @Query(value = "SELECT c " +
            "FROM Car c " +
            "WHERE c.repaired = true")
    List<Car> findAllUnderRepair();

    @Query(value = "SELECT count(o.id) = 0 " +
            "FROM orders o " +
            "JOIN car c on o.car_id = c.id " +
            "WHERE c.id = :id AND o.id IN (SELECT order_id FROM car_rental_time crt WHERE crt.start_rental_date <= :endDate AND " +
            "crt.end_rental_date >= :startDate)", nativeQuery = true)
    boolean isCarAvailable(@Param("id") Long id, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}