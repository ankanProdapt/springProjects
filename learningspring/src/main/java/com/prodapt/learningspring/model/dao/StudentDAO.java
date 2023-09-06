package com.prodapt.learningspring.model.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.prodapt.learningspring.model.Student;

@Repository
public class StudentDAO {
  
  @Autowired
  private JdbcTemplate jdbcTemplate;
  
  
  
  private static final String STUDENT_LIST = "select * from students";
  private static final String READ_STUDENT = "select * from students where id=?";
  private static final String CREATE_STUDENT = "insert into students(name, score) values(?, ?)";
  private static final String UPDATE_STUDENT = "update students set name=?, score=? where id=?";
  private static final String DELETE_STUDENT = "delete from students where id=?";
  
  public Student findById(int id) {
    return jdbcTemplate.query(READ_STUDENT, rs -> {
      rs.next();
      int readId = rs.getInt("id");
      int score = rs.getInt("score");
      String name = rs.getString("name");
      return new Student(readId, name, score);
    }, id);
  }
  
  public List<Student> readAllStudents() {
    return jdbcTemplate.query(STUDENT_LIST, (rs, rowNum) -> {
      int readId = rs.getInt("id");
      int score = rs.getInt("score");
      String name = rs.getString("name");
      return new Student(readId, name, score);
    });
  }
  
  public int createStudent(Student student) {
    return jdbcTemplate.update(CREATE_STUDENT, student.getName(), student.getScore());
  }
  
  public int deleteStudent(Student student) {
    return jdbcTemplate.update(DELETE_STUDENT, student.getId());
  }
  
  public int editStudent(Student student) {
    return jdbcTemplate.update(UPDATE_STUDENT, student.getName(), student.getScore(), student.getId());
  }
  
  
  
}
