package com.prodapt.learningspring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prodapt.learningspring.entity.Post;

public interface PostRepository extends CrudRepository<Post, Integer>{
    @Query(value = "SELECT * FROM post LIMIT ?1", nativeQuery = true)
    List<Post> findAllLimitBy(int limit);

    @Query(value = "SELECT * FROM post WHERE LOWER(CONCAT(post.title, post.content)) LIKE CONCAT('%', ?1, '%')", nativeQuery = true)
    List<Post> searchPostsByPattern(String text);

}
