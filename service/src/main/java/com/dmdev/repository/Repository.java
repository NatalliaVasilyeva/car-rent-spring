package com.dmdev.repository;

import com.dmdev.domain.entity.BaseEntity;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public interface Repository<K, T extends BaseEntity> {

    List<T> findAllHQL(Session session);

    List<T> findAllCriteria(Session session);

    List<T> findAllQueryDsl(Session session);

    Optional<T> findByIdCriteria(Session session, K id);

    Optional<T> findByIdQueryDsl(Session session, K id);
}