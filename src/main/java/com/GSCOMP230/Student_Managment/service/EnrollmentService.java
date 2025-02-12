package com.GSCOMP230.Student_Managment.service;

import com.GSCOMP230.Student_Managment.model.Course;
import com.GSCOMP230.Student_Managment.model.Enrollment;
import com.GSCOMP230.Student_Managment.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    // Fetch an Enrollment by ID
    public Enrollment findById(Long id) {
        return enrollmentRepository.findById(id).orElse(null);
    }

    // Save an Enrollment
    public Enrollment save(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    // Fetch unique courses for a given student
    public Set<Course> getRegisteredCoursesByStudentId(Long studentId) {
        // Fetch all enrollments for the student
        List<Enrollment> enrollments = enrollmentRepository.findByStudent_Id(studentId);

        // Create a set to store unique courses
        Set<Course> courses = new HashSet<>();

        // Iterate through the enrollments and add courses to the set
        for (Enrollment enrollment : enrollments) {
            courses.add(enrollment.getCourse());
        }

        return courses;
    }
}
