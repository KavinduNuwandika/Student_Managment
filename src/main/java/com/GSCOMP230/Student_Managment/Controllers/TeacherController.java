package com.GSCOMP230.Student_Managment.Controllers;

import com.GSCOMP230.Student_Managment.model.Course;
import com.GSCOMP230.Student_Managment.model.Enrollment;
import com.GSCOMP230.Student_Managment.model.User;
import com.GSCOMP230.Student_Managment.dto.StudentGradeInfo;
import com.GSCOMP230.Student_Managment.repository.EnrollmentRepository;
import com.GSCOMP230.Student_Managment.service.CourseService;
import com.GSCOMP230.Student_Managment.service.EnrollmentService;
import com.GSCOMP230.Student_Managment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.GSCOMP230.Student_Managment.DTO.GradeCalculator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class TeacherController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private GradeCalculator gradeCalculator;

    // Method to create a new course
    @GetMapping("/teacher/create-class")
    public String createCourse() {
        return "Teacher/create-class";
    }

    // Method to view students enrolled in a specific course
    @GetMapping("/teacher/class-students")
    public String viewClassStudents(@RequestParam("courseId") Long courseId, Model model) {
        // Fetch the course details and enrolled students using the courseId
        Course course = courseService.getCourseDetails(courseId);
        List<Enrollment> enrollments = enrollmentRepository.findByCourse_CourseId(courseId);

        // Split the enrollments into marked and unmarked lists
        List<StudentGradeInfo> markedStudents = enrollments.stream()
                .filter(enrollment -> enrollment.getMarks() != null) // Filter students with marks
                .map(enrollment -> {
                    User student = enrollment.getStudent();
                    Integer marks = enrollment.getMarks();
                    String grade = gradeCalculator.calculateGrade(marks); // Only calculate grade if marks are not null
                    return new StudentGradeInfo(student, marks, grade);
                })
                .collect(Collectors.toList());

        List<StudentGradeInfo> unmarkedStudents = enrollments.stream()
                .filter(enrollment -> enrollment.getMarks() == null) // Filter students without marks
                .map(enrollment -> {
                    User student = enrollment.getStudent();
                    return new StudentGradeInfo(student, null, null); // No marks and grade for unmarked students
                })
                .collect(Collectors.toList());

        // Add the course details and both marked and unmarked students to the model
        model.addAttribute("selectedCourse", course);
        model.addAttribute("markedStudents", markedStudents);
        model.addAttribute("unmarkedStudents", unmarkedStudents);

        return "Teacher/Class-Students"; // This is the Thymeleaf template name
    }

    // Method to display the page for marking students
    @GetMapping("/teacher/mark-students")
    public String markStudentsPage(@RequestParam("courseId") Long courseId, Model model) {
        Course course = courseService.getCourseDetails(courseId);
        List<Enrollment> enrollments = enrollmentRepository.findByCourse_CourseId(courseId)
                .stream()
                .filter(e -> e.getMarks() == null)
                .collect(Collectors.toList()); // Only unmarked students

        model.addAttribute("course", course);
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("courseId", courseId);

        return "Teacher/mark-students";
    }


    // Method to update marks for students
    @PostMapping("/teacher/mark-students")
    public String markStudents(@RequestParam("courseId") Long courseId,
                               @RequestParam("enrollmentIds") List<Long> enrollmentIds,
                               @RequestParam("marks") List<Integer> marks) {

        for (int i = 0; i < enrollmentIds.size(); i++) {
            Long enrollmentId = enrollmentIds.get(i);
            Integer mark = marks.get(i);

            Enrollment enrollment = enrollmentService.findById(enrollmentId);
            if (enrollment != null) {
                enrollment.setMarks(mark);
                enrollmentService.save(enrollment); // Save each updated enrollment
            }
        }

        return "redirect:/teacher/class-students?courseId=" + courseId;
    }

    @GetMapping("/teacher/edit-marks")
    public String editMarksPage(@RequestParam("courseId") Long courseId, Model model) {
        Course course = courseService.getCourseDetails(courseId);
        List<Enrollment> enrollments = enrollmentRepository.findByCourse_CourseId(courseId)
                .stream()
                .filter(e -> e.getMarks() != null) // Only students with marks
                .collect(Collectors.toList());

        model.addAttribute("course", course);
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("courseId", courseId);

        return "Teacher/edit-marks";
    }

    @PostMapping("/teacher/edit-marks")
    public String editMarks(@RequestParam("courseId") Long courseId,
                            @RequestParam("enrollmentIds") List<Long> enrollmentIds,
                            @RequestParam("marks") List<String> marks) { // Use String to handle null

        for (int i = 0; i < enrollmentIds.size(); i++) {
            Long enrollmentId = enrollmentIds.get(i);
            String markValue = marks.get(i);

            Enrollment enrollment = enrollmentService.findById(enrollmentId);
            if (enrollment != null) {
                if (markValue.isEmpty()) {
                    enrollment.setMarks(null); // Set to null if mark is empty
                } else {
                    enrollment.setMarks(Integer.parseInt(markValue));
                }
                enrollmentService.save(enrollment);
            }
        }

        return "redirect:/teacher/class-students?courseId=" + courseId;
    }

    @GetMapping("/teacher/delete-marks")
    public String deleteMarksPage(@RequestParam("courseId") Long courseId, Model model) {
        Course course = courseService.getCourseDetails(courseId);
        List<Enrollment> enrollments = enrollmentRepository.findByCourse_CourseId(courseId)
                .stream()
                .filter(e -> e.getMarks() != null) // Only students with marks
                .collect(Collectors.toList());

        model.addAttribute("course", course);
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("courseId", courseId);

        return "Teacher/delete-marks";
    }

    @PostMapping("/teacher/delete-marks")
    public String deleteMarks(@RequestParam("courseId") Long courseId,
                              @RequestParam(value = "deleteMarks", required = false) List<Long> deleteMarks) {

        if (deleteMarks != null) {
            for (Long enrollmentId : deleteMarks) {
                Enrollment enrollment = enrollmentService.findById(enrollmentId);
                if (enrollment != null) {
                    enrollment.setMarks(null); // Set marks to null
                    enrollmentService.save(enrollment);
                }
            }
        }

        return "redirect:/teacher/class-students?courseId=" + courseId;
    }





}
