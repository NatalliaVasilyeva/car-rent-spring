package com.dmdev.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "driverLicenses"})
@EqualsAndHashCode(exclude = {"user", "driverLicenses"})
@Builder
@Entity
public class UserDetails extends AuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonBackReference
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Embedded
    private UserContact userContact;

    @Column(nullable = false)
    private LocalDate birthday;

    @Builder.Default
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate registrationDate = LocalDate.now();

    @Builder.Default
    @OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private Set<DriverLicense> driverLicenses = new HashSet<>();

    public void setDriverLicense(DriverLicense driverLicense) {
        driverLicenses.add(driverLicense);
        driverLicense.setUserDetails(this);
    }

    public void setUser(User user) {
        user.setUserDetails(this);
        this.user = user;
    }
}