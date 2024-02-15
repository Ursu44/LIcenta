package com.example.repository;

public interface FireBaseRepository<T>{
    void create(T entity, String role);
    void read();
    void update(T entity, String identifier);
    void updateConfirmation(String token);
}
