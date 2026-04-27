package dao;

import java.util.List;

public interface BaseDao<T> {
    List<T> findAll();
    T insert(T entity);
}