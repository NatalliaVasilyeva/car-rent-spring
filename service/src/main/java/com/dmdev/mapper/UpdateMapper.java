package com.dmdev.mapper;

public interface UpdateMapper<F, T> extends Mapper<F, T> {

    T mapToEntity(F from, T to);

    void merge(F from, T to);
}