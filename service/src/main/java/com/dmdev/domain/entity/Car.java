package com.dmdev.domain.entity;

import com.dmdev.domain.model.Color;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Valid
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    private Long modelId;

    @Enumerated(EnumType.STRING)
    private Color color;

    private String year;

    private String carNumber;

    @NotNull
    @Column(nullable = false, unique = true)
    private String vin;

    @NotNull
    private Boolean isRepaired = Boolean.FALSE;

    private String image;

}