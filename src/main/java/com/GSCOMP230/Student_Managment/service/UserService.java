package com.GSCOMP230.Student_Managment.service;

import com.GSCOMP230.Student_Managment.model.Enrollment;
import com.GSCOMP230.Student_Managment.model.User;
import com.GSCOMP230.Student_Managment.repository.EnrollmentRepository;
import com.GSCOMP230.Student_Managment.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User getUserByPhone(String phone) {
        return userRepository.findByPhone(phone).orElse(null);
    }
    public boolean isEmailOrPhoneExist(String email, String phone) {
        return userRepository.existsByEmail(email) || userRepository.existsByPhone(phone);
    }

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public List<User> getStudentsEnrolledInCourse(Long courseId) {
        // Fetch all enrollments for the given courseId
        List<Enrollment> enrollments = enrollmentRepository.findByCourse_CourseId(courseId);

        // Map the enrollments to a list of users (students)
        return enrollments.stream()
                .map(Enrollment::getStudent)
                .collect(Collectors.toList());
    }


    @Transactional
    public void updateUser(User user) {
        userRepository.save(user);
    }
}
