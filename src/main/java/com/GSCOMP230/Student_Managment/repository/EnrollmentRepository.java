package com.GSCOMP230.Student_Managment.repository;

import com.GSCOMP230.Student_Managment.model.Course;
import com.GSCOMP230.Student_Managment.model.Enrollment;
import com.GSCOMP230.Student_Managment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByCourse_CourseId(Long courseId);

    List<Enrollment> findByStudent_Id(Long studentId);

    Optional<Enrollment> findByCourseAndStudent(Course course, User student);


}
