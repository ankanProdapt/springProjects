package com.prodapt.learningspring.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prodapt.learningspring.dto.CommentDTO;
import com.prodapt.learningspring.entity.Comment;
import com.prodapt.learningspring.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    private Map<Integer, List<Comment>> commentTree;
    private Map<Integer, Integer> level;

    public List<CommentDTO> findAllByPostId(int id){
        return preOrder((List<Comment>) commentRepository.findAllByPostId(id));
    }

    public List<CommentDTO> preOrder(List<Comment> comments){
        commentTree = new HashMap<>();
        level = new HashMap<>();
        List<Comment> preOrderedComments = new ArrayList<>();
        for(Comment comment: comments) {
            commentTree.put(comment.getId(), new ArrayList<>());
            if(comment.getParent() != null){
                commentTree.get(comment.getParent().getId()).add(comment);
            }
        }
        for(Comment comment: comments) {
            if(comment.getParent() == null){
                level.put(comment.getId(), 0);
                preOrderedComments.addAll(dfs(comment, 1));
            }
        }
        List<CommentDTO> commentDTOs = new ArrayList<>();
        for(Comment comment: preOrderedComments){
            CommentDTO c = new CommentDTO();
            c.setId(comment.getId());
            c.setUser(comment.getUser());
            c.setPost(comment.getPost());
            c.setContent(comment.getContent());
            c.setParent(comment.getParent());
            c.setCreatedAt(comment.getCreatedAt());
            c.setUpdatedAt(comment.getUpdatedAt());
            c.setLevel(level.get(comment.getId()));
            commentDTOs.add(c);
        }
        return commentDTOs;
    }

    public List<Comment> dfs(Comment comment, int lvl){
        level.put(comment.getId(), lvl);
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        for(Comment c: commentTree.get(comment.getId())){
            comments.addAll(dfs(c, lvl + 1));
        }
        return comments;
    }
}