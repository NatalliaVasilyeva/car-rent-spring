package com.dmdev.repository;

import com.dmdev.domain.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long>, QuerydslPredicateExecutor<UserDetails> {

    Optional<UserDetails> findByUserId(Long userId);

    List<UserDetails> findAllByNameContainingIgnoreCaseAndSurnameContainingIgnoreCase(String name, String surname);

    List<UserDetails> findByRegistrationDate(LocalDate registrationDate);

    List<UserDetails> findByRegistrationDateBetween(LocalDate start, LocalDate end);
}