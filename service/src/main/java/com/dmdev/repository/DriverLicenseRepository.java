package com.dmdev.repository;

import com.dmdev.domain.entity.DriverLicense;
import com.dmdev.domain.projection.DriverLicenseFullView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DriverLicenseRepository extends JpaRepository<DriverLicense, Long>, QuerydslPredicateExecutor<DriverLicense> {

    Optional<DriverLicense> findByNumberContainingIgnoreCase(String number);

    @Query(value = "SELECT dl " +
            "FROM DriverLicense dl " +
            "JOIN fetch dl.userDetails ud " +
            "JOIN fetch ud.user u " +
            "WHERE u.id  = :id ")
    Optional<DriverLicense> findByUserId(@Param("id") Long userId);

    List<DriverLicense> findByExpiredDateLessThanEqual(LocalDate expiredDate);


    @Query(value = "SELECT dl.id as id, dl.number as number, dl.issueDate as issueDate, dl.expiredDate as expiredDate, " +
            "ud.name as firstname, ud.surname as lastname, ud.userContact.phone as phone " +
            "FROM DriverLicense dl " +
            "JOIN dl.userDetails ud " +
            "WHERE dl.issueDate >= :issueDate AND dl.expiredDate <= :expiredDate " +
            "ORDER BY ud.surname ASC ")
    List<DriverLicenseFullView> findDriverLicensesFullViewByIssueAndExpiredDate(@Param("issueDate") LocalDate issueDate, @Param("expiredDate") LocalDate expiredDate);
}