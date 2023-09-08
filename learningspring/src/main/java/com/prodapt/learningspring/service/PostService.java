package com.prodapt.learningspring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prodapt.learningspring.dto.PostDTO;
import com.prodapt.learningspring.entity.Post;
import com.prodapt.learningspring.repository.CommentRepository;
import com.prodapt.learningspring.repository.LikeCRUDRepository;
import com.prodapt.learningspring.repository.PostRepository;

@Service
public class PostService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeCRUDRepository likeCRUDRepository;

    @Autowired
    private PostRepository postRepository;

    public PostDTO convert(Post post){
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setAuthor(post.getAuthor());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setLikesCount(likeCRUDRepository.countByLikeIdPost(post));
        postDTO.setCommentsCount(commentRepository.findAllByPostId(post.getId()).size());
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setUpdatedAt(post.getUpdatedAt());
        return postDTO;
    }

    public PostDTO findById(int id){
        return convert(postRepository.findById(id).get());
    }

    public List<PostDTO> findAll(){
        return convert((List<Post>) postRepository.findAll());
    }

    public List<PostDTO> convert(List<Post> postList){
        List<PostDTO> posts = new ArrayList<>();

        for(Post post: postList){
            posts.add(convert(post));
        }

        return posts;
    } 
}
