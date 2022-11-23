package com.dmdev.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"price", "models"})
@EqualsAndHashCode(of = "name")
@Builder
@Entity
public class Category implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @Column(nullable = false, unique = true)
    private String name = "economy";

    @Column(nullable = false, unique = true, precision = 12, scale = 2)
    private BigDecimal price;

    @Builder.Default
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Car> cars = new HashSet<>();

//    public void setCar(Car car) {
//        cars.add(car);
//        car.setCategory(this);
//    }
}