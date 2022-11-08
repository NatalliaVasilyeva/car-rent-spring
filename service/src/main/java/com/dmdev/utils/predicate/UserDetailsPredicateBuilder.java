package com.dmdev.utils.predicate;

import com.dmdev.domain.dto.filterdto.UserDetailsFilter;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;

import static com.dmdev.domain.entity.QUserDetails.userDetails;

@Component
public class UserDetailsPredicateBuilder implements PredicateBuilder<Predicate, UserDetailsFilter> {

    @Override
    public Predicate build(UserDetailsFilter requestFilter) {
        return QPredicate.builder()
                .add(requestFilter.getName(), userDetails.name::containsIgnoreCase)
                .add(requestFilter.getSurname(), userDetails.surname::containsIgnoreCase)
                .add(requestFilter.getBirthday(), userDetails.birthday::eq)
                .add(requestFilter.getPhone(), userDetails.userContact.phone::eq)
                .add(requestFilter.getAddress(), userDetails.userContact.address::containsIgnoreCase)
                .buildAnd();
    }
}