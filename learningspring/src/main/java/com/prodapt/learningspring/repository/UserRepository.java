package com.prodapt.learningspring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prodapt.learningspring.entity.User;

public interface UserRepository extends CrudRepository<User, Integer>{
  Optional<User> findByName(String name); 
  Integer countByName(String name);
  @Query(value = "select * from user where name = ?1", nativeQuery = true)
  User findBySomeConstraintSpringCantParse(String name);
}
