package com.dmdev.repository;

import com.dmdev.domain.entity.Order;
import com.dmdev.domain.model.OrderStatus;
import com.dmdev.domain.projection.OrderFullView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, QuerydslPredicateExecutor<Order> {

    @Query(value = "SELECT o " +
            "FROM Order o " +
            "JOIN fetch o.car c " +
            "WHERE c.carNumber  = :carNumber")
    List<Order> findAllByCarNumber(@Param("carNumber") String carNumber);

    @Query(value = "SELECT o " +
            "FROM Order o " +
            "JOIN fetch o.car c " +
            "WHERE c.id  = :carId")
    List<Order> findAllByCarId(@Param("carId") Long carId);

    List<Order> findAllByOrderStatus(OrderStatus orderStatus);

    @Query(value = "SELECT o " +
            "FROM Order o " +
            "JOIN fetch o.user u " +
            "WHERE u.id  = :userId")
    List<Order> findAllByUserId(@Param("userId") Long userId);

    List<Order> findAllByDateBetween(LocalDate start, LocalDate end);

    List<Order> findAllByDate(LocalDate date);

    @Query(value = "SELECT o " +
            "FROM Order o " +
            "WHERE o.accidents.size > 0")
    List<Order> findAllWithAccidents();


    @Query(value = "SELECT o.id as id, " +
            "o.date as date, " +
            "o.insurance as insurance, " +
            "o.orderStatus as orderStatus, " +
            "o.sum as sum, " +
            "crt.startRentalDate as startRentalDate, " +
            "crt.endRentalDate as endRentalDate, " +
            "c.carNumber as carNumber, " +
            "m.name as modelName, " +
            "b.name as brandName, " +
            "ud.name as firstname, " +
            "ud.surname as surname, " +
            "ud.userContact.phone as phone,  " +
            "CASE WHEN o.accidents.size > 0 THEN true ELSE false END " +
            "FROM Order o " +
            "JOIN o.carRentalTime crt " +
            "JOIN o.car c " +
            "JOIN o.user u " +
            "JOIN u.userDetails ud " +
            "JOIN c.model m " +
            "JOIN m.brand b ")
    List<OrderFullView> findAllFullView();
}