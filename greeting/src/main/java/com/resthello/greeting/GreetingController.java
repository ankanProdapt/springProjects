package com.resthello.greeting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/classroom")
@CrossOrigin
public class GreetingController {
    @Autowired
    private Repository repository;

    @GetMapping("/students")
    @ResponseBody
    public List<Student> getStudentList(){
        System.out.println(123);
        return (List<Student>)repository.findAll();
    }

    @GetMapping("/add")
    public String addStudent(@RequestParam(name = "name") String name, @RequestParam(name = "marks") int marks) {
        System.out.println(111);
        Student student = new Student();
        student.setName(name);
        student.setMarks(marks);
        repository.save(student);
        return "Added Successfully!!!";
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody Student student) {
        repository.save(student);
        return "Saved!";
    }
}
