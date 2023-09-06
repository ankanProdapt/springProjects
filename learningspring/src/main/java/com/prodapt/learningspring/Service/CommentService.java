package com.prodapt.learningspring.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.prodapt.learningspring.entity.Comment;

@Service
public class CommentService {
    public List<Comment> preOrder(List<Comment> comments){
        List<Comment> preOrderedComments = new ArrayList<>();
        for(Comment comment: comments) {
            if(comment.getParent() == null) {
                preOrderedComments.add(comment);
            }
            else {
                for(int i = 0; i < preOrderedComments.size(); i++) {
                    if(preOrderedComments.get(i).getId() == comment.getParent().getId()){
                        System.out.println(comment.getParent());
                        preOrderedComments.add(i + 1, comment);
                        break;
                    }
                }
            }
        }
        return preOrderedComments;
    }
}
