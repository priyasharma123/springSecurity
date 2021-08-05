package com.spring.security.controller;

import com.spring.security.model.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/student/")
public class StudentController {

    List<Student> studentList = Arrays.asList(
        new Student(1,"priya"),
        new Student(2,"piu"),
        new Student(3,"gudia")
    );

    @GetMapping("{id}")
    public Student getStudent(@PathVariable("id") Integer id){
        System.out.println("Printing id : " + id +"\n");
      return  studentList.stream().filter(student -> id.equals(student.getId())).findFirst().orElseThrow(()->new IllegalStateException("Student : "+ id));

    }

//    @GetMapping("/")
//    public String getHello(){
//        return  "hello";
//
//    }
}
