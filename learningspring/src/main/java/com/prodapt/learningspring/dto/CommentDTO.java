package com.prodapt.learningspring.dto;

import java.util.Date;

import com.prodapt.learningspring.entity.Comment;
import com.prodapt.learningspring.entity.Post;
import com.prodapt.learningspring.entity.User;

import lombok.Data;


@Data
public class CommentDTO {
    private int id;
    private User user;
    private Post post;
    private Comment parent;
    private int level;
    private String content;
    private Date createdAt;
    private Date updatedAt;
}
