package com.dmdev.utils.predicate;

public interface PredicateBuilder<R, T> {

    R build(T requestFilter);
}