package com.prodapt.learningspring.repository;

import org.springframework.data.repository.CrudRepository;

import com.prodapt.learningspring.entity.Tag;

public interface TagRepository extends CrudRepository<Tag, Integer> {
    
}
