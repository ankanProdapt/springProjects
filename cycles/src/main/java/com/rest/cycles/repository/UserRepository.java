package com.rest.cycles.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.rest.cycles.entity.User;

public interface UserRepository extends CrudRepository<User, Long>{
    public Optional<User> findByName(String name);

    public boolean existsByName(String name);
}