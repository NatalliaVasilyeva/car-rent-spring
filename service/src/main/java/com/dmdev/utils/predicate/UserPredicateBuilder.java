package com.dmdev.utils.predicate;

import com.dmdev.domain.dto.filterdto.UserFilter;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;

import static com.dmdev.domain.entity.QUser.user;

@Component
public class UserPredicateBuilder implements PredicateBuilder<Predicate, UserFilter> {

    @Override
    public Predicate build(UserFilter requestFilter) {
        return QPredicate.builder()
                .add(requestFilter.getUsername(), user.username::eq)
                .add(requestFilter.getEmail(), user.email::eq)
                .add(requestFilter.getName(), user.userDetails.name::containsIgnoreCase)
                .add(requestFilter.getSurname(), user.userDetails.surname::containsIgnoreCase)
                .add(requestFilter.getBirthday(), user.userDetails.birthday::eq)
                .add(requestFilter.getExpiredLicenseDriverDate(), user.userDetails.driverLicenses.any().expiredDate::loe)
                .buildAnd();
    }
}