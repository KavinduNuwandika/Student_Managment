package com.GSCOMP230.Student_Managment.Controllers;

import com.GSCOMP230.Student_Managment.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import com.GSCOMP230.Student_Managment.model.Course;
import com.GSCOMP230.Student_Managment.model.Enrollment;
import com.GSCOMP230.Student_Managment.service.EnrollmentService;
import org.springframework.web.bind.annotation.RequestParam;
import com.GSCOMP230.Student_Managment.DTO.GradeCalculator;


import java.util.*;


@Controller
public class StudentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;



    @GetMapping("/student/registered-courses")
    public String viewRegisteredCourses(@RequestParam Long studentId, Model model) {
        Set<Course> registeredCourses = enrollmentService.getRegisteredCoursesByStudentId(studentId);
        model.addAttribute("courses", registeredCourses);
        return "Student/registered-courses"; // Return the appropriate view
    }

    @GetMapping("/student/view-results")
    public String viewResults(@RequestParam("id") Long userId, Model model) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudent_Id(userId);

        List<Map<String, Object>> markedSubjects = new ArrayList<>();
        List<Enrollment> unmarkedSubjects = new ArrayList<>();

        GradeCalculator gradeCalculator = new GradeCalculator();
        double totalGradePoints = 0.0;
        int totalCredits = 0;

        for (Enrollment enrollment : enrollments) {
            if (enrollment.getMarks() != null) {
                String grade = gradeCalculator.calculateGrade(enrollment.getMarks());
                double gradePoints = gradeCalculator.getGradePoints(grade);

                // Add grade points and credits to the total
                totalGradePoints += gradePoints * enrollment.getCourse().getCredits();
                totalCredits += enrollment.getCourse().getCredits();

                // Prepare data for view
                Map<String, Object> entry = new HashMap<>();
                entry.put("enrollment", enrollment);
                entry.put("grade", grade);
                markedSubjects.add(entry);
            } else {
                unmarkedSubjects.add(enrollment);
            }
        }

        // Calculate GPA
        double gpa = totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;

        model.addAttribute("markedSubjects", markedSubjects);
        model.addAttribute("unmarkedSubjects", unmarkedSubjects);
        model.addAttribute("currentGPA", String.format("%.2f", gpa)); // Format GPA to 2 decimal places

        return "Student/view-results";
    }

}