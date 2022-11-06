package com.dmdev.utils.predicate;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        return ExpressionUtils.allOf(predicates);
    }

    public Predicate buildOr() {
        return ExpressionUtils.anyOf(predicates);
    }
}