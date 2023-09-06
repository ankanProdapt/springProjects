package com.prodapt.learningspring.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.prodapt.learningspring.model.dao.StudentDAO;

@Component
public class ClassroomService {
  private List<Student> students;
  private static int idCounter = 1;
  
  @Autowired
  private StudentDAO studentDAO;

  public ClassroomService() {
    if (students == null)
      students = new ArrayList<>();
  }
  
  public void sync() {
    students = studentDAO.readAllStudents();
    rank();
  }
  
  public List<Student> getStudents() {
    if (students.isEmpty())
      sync();
    return Collections.unmodifiableList(students);
  }

  private void rank() {
    Collections.sort(students, (s1, s2) -> -Integer.compare(s1.getScore(), s2.getScore()));
    for (int i = 0; i < students.size(); i++)
      students.get(i).setRank(i + 1);
    for (int i = 1; i < students.size(); i++) {
      if (students.get(i).getScore() == students.get(i-1).getScore())
        students.get(i).setRank(students.get(i-1).getRank());
    }
  }

  public void add(Student student) {
    student.setId(idCounter++);
    students.add(student);
    studentDAO.createStudent(student);
    sync();
  }
  
  public void remove(int idx) {
    students.remove(idx);
    rank();
  }
  
  public void replace(int id, Student current) {
    for (int i = 0; i < students.size(); i++) {
      if (students.get(i).getId() == id) {
        students.get(i).setName(current.getName());
        students.get(i).setScore(current.getScore());
      }
    }
    rank();
  }
  
  public Optional<Student> getById(int id) {
    for (int i = 0; i < students.size(); i++) {
      if (students.get(i).getId() == id)
        return Optional.of(students.get(i));
    }
    return Optional.empty();
  }


}
