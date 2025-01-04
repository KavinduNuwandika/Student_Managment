package com.GSCOMP230.Student_Managment.service;

import com.GSCOMP230.Student_Managment.model.Course;
import com.GSCOMP230.Student_Managment.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public Course getCourseDetails(Long courseId) {
        return courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));
    }
}
