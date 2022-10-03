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
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "categories")
@EqualsAndHashCode(exclude = "categories")
@Builder
@Entity
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true, precision = 12, scale = 2, columnDefinition = "numeric check(sum > 0)")
    private BigDecimal sum;

    @Builder.Default
    @OneToMany(mappedBy = "price", cascade = CascadeType.ALL)
    private Set<Category> categories = new HashSet<>();

    public void setCategory(Category category) {
        categories.add(category);
        category.setPrice(this);
    }
}