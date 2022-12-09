package com.dmdev.mapper;

public interface CreateMapper<F, T> extends Mapper<F, T> {

    T mapToEntity(F dto);
}