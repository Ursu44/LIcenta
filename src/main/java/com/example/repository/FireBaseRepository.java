package com.example.repository;

public interface FireBaseRepository<T>{
    void create(T entity);
    void read();
}
