package com.spring.security.controller;

import com.spring.security.model.Student;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/management/api/v1/student/")
public class ManagementController {

    List<Student> studentList = Arrays.asList(
            new Student(1,"priya"),
            new Student(2,"piu"),
            new Student(3,"gudia")
    );

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ADMINTRANIEE')")//using annotation based roles
    public List<Student> getStudentList() {

        return studentList;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('student:write')")
    public void registerStudent(@RequestBody Student student){
        System.out.println("Student : " + student);
    }

    @DeleteMapping(path ="{studentID}")
    @PreAuthorize("hasAuthority('student:write')")
    public void deleteStudent(@PathVariable Integer studentID){
        System.out.println("Student ID : " + studentID);
    }

    @PutMapping(path ="{studentID}")
    @PreAuthorize("hasAuthority('student:write')")
    public void updateStudent(@PathVariable Integer studentID,@RequestBody Student student){
        System.out.println("Student ID : " + studentID + " " + student);
    }

}
