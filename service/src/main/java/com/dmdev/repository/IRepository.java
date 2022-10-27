package com.dmdev.repository;

import com.dmdev.domain.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface IRepository<K extends Serializable, E extends BaseEntity<K>> {

    E save(E entity);

    void delete(E entity);

    void update(E entity);

    Optional<E> findById(K id);

    List<E> findAll();
}