package com.dmdev.utils.predicate;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QPredicate {

    private final List<Predicate> predicates = new ArrayList<>();

    public static QPredicate builder() {
        return new QPredicate();
    }

    public <T> QPredicate add(T object, Function<T, Predicate> function) {
        if (object != null) {
            predicates.add(function.apply(object));
        }
        return this;
    }

    public <T> QPredicate add(Collection<? extends T> collection, Function<Collection<? extends T>, Predicate> function) {
        if (CollectionUtils.isNotEmpty(collection)) {
            predicates.add(function.apply(collection));
        }
        return this;
    }

    public QPredicate addPredicate(Predicate predicate) {
        predicates.add(predicate);
        return this;
    }

    public Predicate buildAnd() {

        return Optional.ofNullable(ExpressionUtils.allOf(predicates)).orElseGet(() -> Expressions.asBoolean(true).isTrue());
    }

    public Predicate buildOr() {

        return Optional.ofNullable(ExpressionUtils.anyOf(predicates)).orElseGet(() -> Expressions.asBoolean(true).isTrue());
    }
}