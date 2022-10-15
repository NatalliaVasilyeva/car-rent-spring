package com.dmdev.utils.predicate;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CriteriaPredicate {
    private final List<Predicate> predicates = new ArrayList<>();

    public CriteriaPredicate() {
        //Do nothing (sonar lint)
    }

    public static CriteriaPredicate builder() {
        return new CriteriaPredicate();
    }

    public <T> CriteriaPredicate add(T object, Function<T, Predicate> function) {
        if (object != null) {
            predicates.add(function.apply(object));
        }
        return this;
    }

    public CriteriaPredicate buildOr(Predicate first, Predicate second, CriteriaBuilder cb) {
        predicates.add(cb.or(first, second));
        return this;
    }

    public Predicate[] getPredicates() {
        return predicates.toArray(Predicate[]::new);
    }
}