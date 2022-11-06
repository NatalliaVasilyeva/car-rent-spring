package com.dmdev.repository;

import com.dmdev.domain.entity.Accident;
import com.dmdev.domain.projection.AccidentFullView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface AccidentRepository extends JpaRepository<Accident, Long>, QuerydslPredicateExecutor<Accident> {

    List<Accident> findAllByAccidentDateOrderByAccidentDateDesc(LocalDate localDate);

    List<Accident> findAllByAccidentDateBetween(LocalDate firstDate, LocalDate secondDate);

    List<Accident> findAllByDamageIsGreaterThanEqualOrderByDamageDesc(BigDecimal damage);

    @Query(value = "SELECT a " +
            "FROM Accident a " +
            "JOIN fetch a.order o " +
            "WHERE o.id = :orderId")
    List<Accident> findAllByOrderId(@Param("orderId") Long orderId);

    @Query(value = "SELECT a " +
            "FROM Accident a " +
            "JOIN fetch a.order o " +
            "JOIN fetch o.user u " +
            "JOIN fetch u.userDetails ud " +
            "WHERE lower(ud.name) = lower(:name) AND lower(ud.surname) = lower(:surname)")
    List<Accident> findAllByNameAndSurname(@Param("name") String name, @Param("surname") String surname);

    @Query(value = "SELECT a " +
            "FROM Accident a " +
            "JOIN fetch a.order o " +
            "JOIN fetch o.car c " +
            "WHERE c.carNumber like %:number% " +
            "order by a.accidentDate")
    List<Accident> findAllByCarNumber(@Param("number") String carNumber);

    @Query(value = "SELECT a " +
            "FROM Accident a " +
            "WHERE a.damage > (SELECT avg(va.damage) FROM Accident va)")
    List<Accident> findAllByAvgDamageMore();

    @Query(value = "SELECT a.id as id, a.accidentDate as accidentDate, a.description as description, a.damage as damage, o.id as orderId, " +
            "b.name as brandName, m.name as modelName, c.carNumber as carNumber, ud.name as firstname, ud.surname as surname " +
            "FROM Accident a " +
            "JOIN a.order o " +
            "JOIN o.car c " +
            "JOIN c.model m " +
            "JOIN m.brand b " +
            "JOIN o.user u " +
            "JOIN u.userDetails ud")
    List<AccidentFullView> findAllFull();

    @Query(value = "SELECT a.id as id, a.accidentDate as accidentDate, a.description as description, a.damage as damage, o.id as orderId, " +
            "b.name as brandName, m.name as modelName, c.carNumber as carNumber, ud.name as firstname, ud.surname as surname " +
            "FROM Accident a " +
            "JOIN a.order o " +
            "JOIN o.car c " +
            "JOIN c.model m " +
            "JOIN m.brand b " +
            "JOIN o.user u " +
            "JOIN u.userDetails ud " +
            "WHERE a.id = :id")
    List<AccidentFullView> findByIdFull(@Param("id") Long id);
}