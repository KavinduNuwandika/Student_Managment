package com.GSCOMP230.Student_Managment.repository;
import com.GSCOMP230.Student_Managment.model.Course;
import java.util.List;
import java.util.Optional;

import com.GSCOMP230.Student_Managment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTeacher(User teacher);
    boolean existsByCourseCodeAndAcademicYear(String courseCode, int academicYear);
    List<Course> findAll();

    Optional<Course> findByCourseId(Long courseId);


}