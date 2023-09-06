package com.prodapt.learningspring.repository;

import org.springframework.data.repository.CrudRepository;

import com.prodapt.learningspring.entity.Comment;

public interface CommentRepository extends CrudRepository<Comment, Integer>{

}
