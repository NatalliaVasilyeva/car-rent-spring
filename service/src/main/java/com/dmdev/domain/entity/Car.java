package com.dmdev.domain.entity;

import com.dmdev.domain.model.Color;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"model", "brand", "category", "orders"})
@EqualsAndHashCode(of = "vin")
@Builder
@Entity
public class Car implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    @JsonBackReference
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    @JsonBackReference
    private Model model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @Enumerated(EnumType.STRING)
    private Color color;

    private Integer year;

    private String carNumber;

    @Column(nullable = false, unique = true)
    private String vin;

    @Builder.Default
    private boolean repaired = true;

    private String image;

    @Builder.Default
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Order> orders = new ArrayList<>();

    public void setCategory(Category category) {
        this.category = category;
        this.category.getCars().add(this);
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
        this.brand.getCars().add(this);
    }

    public void setModel(Model model) {
        this.model = model;
        this.model.getCars().add(this);
    }
}