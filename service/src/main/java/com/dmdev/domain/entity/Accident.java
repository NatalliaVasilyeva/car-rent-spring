package com.dmdev.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Valid
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Accident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long orderId;

    @NotNull
    @Column(nullable = false)
    private LocalDate accidentDate;

    private String description;

    @NotNull
    @Column(precision = 12, scale = 2)
    private BigDecimal damage;
}