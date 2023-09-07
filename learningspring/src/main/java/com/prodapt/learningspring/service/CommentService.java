package com.prodapt.learningspring.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.prodapt.learningspring.entity.Comment;

@Service
public class CommentService {

    private Map<Integer, List<Comment>> commentTree;

    public List<Comment> preOrder(List<Comment> comments){
        commentTree = new HashMap<>();
        List<Comment> preOrderedComments = new ArrayList<>();
        for(Comment comment: comments) {
            commentTree.put(comment.getId(), new ArrayList<>());
            if(comment.getParent() != null){
                commentTree.get(comment.getParent().getId()).add(comment);
            }
        }
        for(Comment comment: comments) {
            if(comment.getParent() == null){
                preOrderedComments.addAll(dfs(comment));
            }
        }
        return preOrderedComments;
    }

    public List<Comment> dfs(Comment comment){
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        for(Comment c: commentTree.get(comment.getId())){
            comments.addAll(dfs(c));
        }
        return comments;
    }
}