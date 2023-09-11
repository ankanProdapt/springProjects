package com.prodapt.learningspring.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.prodapt.learningspring.entity.Post;

public interface PostRepository extends CrudRepository<Post, Integer>{
    @Query(value = "SELECT COUNT(*) FROM post WHERE post.author_id = ?1 AND DATE(post.created_at) = DATE(NOW())", nativeQuery = true)
    int CountPostsCreatedTodayBy(int id);

    @Query(value = "SELECT COUNT(*) FROM post WHERE post.author_id = ?1 AND MONTH(post.created_at) =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 MONTH(NOW())", nativeQuery = true)
    int CountPostsCreatedInThisMonthBy(int id);
}
