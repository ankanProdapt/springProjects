package com.prodapt.learningspring.model;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Component
@Data
public class Student {
  private int id;
  private String name;
  @Min(value = 0) private int score;
  private int rank;  
  
  public Student() {
    
  }
  
  public Student(int id, String name, int score) {
    this.id = id;
    this.name = name;
    this.score = score;
  }
}
