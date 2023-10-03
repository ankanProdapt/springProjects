package com.prodapt.learningspring.dto;

import java.util.Date;
import java.util.List;

import com.prodapt.learningspring.entity.User;

import lombok.Data;

@Data
public class PostDTO {
    private int id;
    private User author;
    private String title;
    private String content;
    private int likesCount;
    private int commentsCount;
    private Date createdAt;
    private Date updatedAt;
    private List<CommentDTO> comments;
}
