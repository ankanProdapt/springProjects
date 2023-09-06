package com.prodapt.learningspring.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class LikeRecord {
  
  @EmbeddedId
  private LikeId likeId;
  
}
