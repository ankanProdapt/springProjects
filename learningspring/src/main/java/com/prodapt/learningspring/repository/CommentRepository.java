package com.prodapt.learningspring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prodapt.learningspring.entity.Comment;

public interface CommentRepository extends CrudRepository<Comment, Integer>{
    @Query(value = "SELECT * FROM comment WHERE post_id = ?1 LIMIT ?2", nativeQuery = true)
    List<Comment> findAllByPostIdLimitBy(int postId, int limit);

    @Query(value = "SELECT * FROM comment WHERE post_id = ?1", nativeQuery = true)
    List<Comment> findAllByPostId(int postId);
}