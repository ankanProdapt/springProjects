package com.prodapt.learningspring.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.prodapt.learningspring.entity.User;


public interface UserRepository extends CrudRepository<User, Long>{
    public Optional<User> findByName(String name);
    public boolean existsByName(String name);
}

