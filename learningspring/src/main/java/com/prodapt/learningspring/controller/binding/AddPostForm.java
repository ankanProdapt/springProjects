package com.prodapt.learningspring.controller.binding;

import lombok.Data;

@Data
public class AddPostForm {
  private String title;
  private String content;
  private int userId;
}
