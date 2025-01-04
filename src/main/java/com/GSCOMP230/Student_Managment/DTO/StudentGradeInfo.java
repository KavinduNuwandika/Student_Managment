package com.GSCOMP230.Student_Managment.dto;

import com.GSCOMP230.Student_Managment.model.User;

public class StudentGradeInfo {

    private User student;
    private Integer marks;
    private String grade;

    // Constructor with Integer for marks to handle null values
    public StudentGradeInfo(User student, Integer marks, String grade) {
        this.student = student;
        this.marks = marks;
        this.grade = grade;
    }

    // Getters and setters for all fields
    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
