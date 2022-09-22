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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Valid
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarRentalTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long orderId;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime startRentalDate;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime endRentalDate;
}