package com.dmdev.repository;

import com.dmdev.domain.entity.CarRentalTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CarRentalTimeRepository extends JpaRepository<CarRentalTime, Long>, QuerydslPredicateExecutor<CarRentalTime> {

    @Query(value = "SELECT crt " +
            "FROM CarRentalTime crt " +
            "WHERE crt.order.id  = :orderId ")
    Optional<CarRentalTime> findByOrderId(@Param("orderId") Long orderId);
}