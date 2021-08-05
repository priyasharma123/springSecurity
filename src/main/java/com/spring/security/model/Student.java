package com.spring.security.model;

public class Student {
    private Integer Id;
    private String name;

    public Student(Integer id, String name) {
        Id = id;
        this.name = name;
    }

    public int getId() {
        return Id;
    }

    @Override
    public String toString() {
        return "Student{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                '}';
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
