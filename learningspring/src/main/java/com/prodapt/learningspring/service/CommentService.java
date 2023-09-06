package com.prodapt.learningspring.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prodapt.learningspring.entity.Comment;
import com.prodapt.learningspring.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> preOrder(List<Comment> comments){
        List<Comment> preOrderedComments = new ArrayList<>();
        for(Comment comment: comments) {
            if(comment.getParent() == null) {
                preOrderedComments.add(comment);
                preOrderedComments.addAll(dfs(comment));
            }
            
        }
        return preOrderedComments;
    }

    public List<Comment> dfs(Comment comment){
        List<Comment> replies = new ArrayList<>();
        commentRepository.findAllByParent(comment.getId()).forEach(replies::add);
        List<Comment> preOrdComments = new ArrayList<>();
        for(Comment c: replies){
            preOrdComments.add(c);
            preOrdComments.addAll(dfs(c));
        }
        return preOrdComments;
    }
}