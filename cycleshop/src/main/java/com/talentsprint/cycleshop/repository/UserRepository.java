package com.talentsprint.cycleshop.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.talentsprint.cycleshop.entity.User;

public interface UserRepository extends CrudRepository<User, Long>{
    public Optional<User> findByName(String name);
}
