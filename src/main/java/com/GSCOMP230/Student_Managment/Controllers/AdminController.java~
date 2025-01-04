package com.GSCOMP230.Student_Managment.Controllers;

import com.GSCOMP230.Student_Managment.model.Course;
import com.GSCOMP230.Student_Managment.model.Enrollment;
import com.GSCOMP230.Student_Managment.model.User;
import com.GSCOMP230.Student_Managment.repository.EnrollmentRepository;
import com.GSCOMP230.Student_Managment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.GSCOMP230.Student_Managment.repository.CourseRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @GetMapping("/admin/home")
    public String showAdminHome(HttpSession session, Model model) {
        String email = (String) session.getAttribute("userEmail");
        if (email != null) {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent() && userOpt.get().getRole().equals("Admin")) {
                model.addAttribute("user", userOpt.get());
                return "Admin/AdminHome";
            }
        }
        return "redirect:/login"; // Redirect to login if not authorized
    }

    // Show the user list with optional role filter
    @GetMapping("/admin/userlist")
    public String showUserList(@RequestParam(name = "roleFilter", required = false) String roleFilter,
                               HttpSession session, Model model) {
        String email = (String) session.getAttribute("userEmail");
        if (email != null) {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent() && userOpt.get().getRole().equals("Admin")) {
                model.addAttribute("user", userOpt.get());

                List<User> userList;
                if (roleFilter != null && !roleFilter.isEmpty()) {
                    userList = userRepository.findByRole(roleFilter); // Find users by role
                    model.addAttribute("roleFilter", roleFilter); // Keep the selected filter value in the model
                } else {
                    userList = userRepository.findAll(); // Fetch all users if no filter
                }

                model.addAttribute("userList", userList); // Add user list to the model
                return "Admin/UserList"; // Return the user list view
            }
        }
        return "redirect:/login"; // Redirect if not authorized or session not found
    }

    // Handle role update from the form submission
    @PostMapping("/admin/updateUserRoles")
    public String updateUserRoles(@RequestParam("userId") Long userId,
                                  @RequestParam("role") String newRole,
                                  HttpSession session) {
        String email = (String) session.getAttribute("userEmail");
        if (email != null) {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setRole(newRole); // Update the role
                userRepository.save(user); // Save the updated user
            }
        }
        return "redirect:/admin/userlist"; // Redirect back to user list page
    }

    // Handle user deletion from the form submission
    @PostMapping("/admin/deleteUser")
    public String deleteUser(@RequestParam("userId") Long userId, HttpSession session) {
        String email = (String) session.getAttribute("userEmail");
        if (email != null) {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                userRepository.delete(user); // Delete the user
            }
        }
        return "redirect:/admin/userlist"; // Redirect back to user list page
    }

    @GetMapping("/admin/view-courses")
    public String viewCoursesForAdmin(Model model) {
        List<Course> courses = courseRepository.findAll(); // Get all courses
        List<User> teachers = userRepository.findByRole("TEACHER"); // Fetch all teachers with the role "TEACHER"

        model.addAttribute("courses", courses);
        model.addAttribute("teachers", teachers); // Add teachers list for dropdown
        return "Admin/View-Courses"; // View to display the courses and teachers
    }

    // Handle the update of a course's teacher
    @PostMapping("/admin/update-course-teacher")
    public String updateCourseTeacher(
            @RequestParam Long courseId,
            @RequestParam Long teacherId,
            Model model) {
        try {
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));

            // Fetch the teacher using the teacherId
            User teacher = userRepository.findById(teacherId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid teacher ID"));

            // Assign the teacher to the course
            course.setTeacher(teacher);
            courseRepository.save(course); // Save the updated course


            model.addAttribute("success", "Teacher updated successfully for course: " + course.getCourseName());
        } catch (Exception e) {
            model.addAttribute("error", "Error updating course teacher: " + e.getMessage());
        }

        return "redirect:/admin/view-courses"; // Redirect back to the course view page
    }

    // Handle the deletion of a course
    @PostMapping("/admin/delete-course")
    public String deleteCourse(@RequestParam("courseId") Long courseId, Model model) {
        try {
            courseRepository.deleteById(courseId); // Delete the course
            model.addAttribute("success", "Course deleted successfully.");
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting course: " + e.getMessage());
        }
        return "redirect:/admin/view-courses"; // Redirect back to the course view page
    }

    @GetMapping("/admin/enroll-students")
    public String getEnrollStudentsPage(
            @RequestParam(name = "courseId", required = false) Long courseId,
            @RequestParam(name = "regYear", required = false) Integer regYear,
            Model model) {

        // Fetch all unique registration years
        List<Integer> uniqueRegYears = userRepository.findDistinctRegYears();

        // Fetch all students with the role "Student"
        List<User> students = userRepository.findByRole("Student");

        // If a regYear filter is applied, filter students further
        if (regYear != null) {
            students = students.stream()
                    .filter(student -> regYear.equals(student.getRegYear()))
                    .collect(Collectors.toList());
        }

        // Prepare the list of unenrolled students (if a course is selected)
        List<User> unenrolledStudents = new ArrayList<>();
        if (courseId != null) {
            Course selectedCourse = courseRepository.findByCourseId(courseId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));
            // Add course details to the model
            model.addAttribute("selectedCourseName", selectedCourse.getCourseName());
            model.addAttribute("selectedCourseYear", selectedCourse.getAcademicYear());
            model.addAttribute("selectedCourseCredits", selectedCourse.getCredits());
            model.addAttribute("selectedCourseId", courseId);


            List<Long> enrolledStudentIds = enrollmentRepository.findByCourse_CourseId(courseId).stream()
                    .map(enrollment -> enrollment.getStudent().getId())
                    .collect(Collectors.toList());

            unenrolledStudents = students.stream()
                    .filter(student -> !enrolledStudentIds.contains(student.getId()))
                    .collect(Collectors.toList());

            // Fetch enrolled students (not just their IDs, but the full User objects)
            List<User> enrolledStudents = userRepository.findAllById(enrolledStudentIds);
            model.addAttribute("enrolledStudents", enrolledStudents);
        }

        // Add attributes to the model
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("uniqueRegYears", uniqueRegYears);
        model.addAttribute("regYear", regYear);
        model.addAttribute("unenrolledStudents", unenrolledStudents);
        model.addAttribute("selectedCourseId", courseId);


        return "Admin/Enroll-Students";
    }

    // Enroll a student in a course
    @PostMapping("/admin/enroll-students")
    public String enrollMultipleStudents(@RequestParam Long courseId, @RequestParam List<Long> studentIds, RedirectAttributes redirectAttributes) {
        try {
            // Fetch the course
            Course course = courseRepository.findByCourseId(courseId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));

            // Enroll each student
            for (Long studentId : studentIds) {
                User student = userRepository.findById(studentId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid student ID"));

                // Check if the student is already enrolled in the course
                if (!enrollmentRepository.findByCourseAndStudent(course, student).isPresent()) {
                    // Create a new enrollment record
                    Enrollment enrollment = new Enrollment();
                    enrollment.setCourse(course);
                    enrollment.setStudent(student);
                    enrollmentRepository.save(enrollment);
                }
            }

            redirectAttributes.addFlashAttribute("success", "Selected students enrolled successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Error enrolling students: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Unexpected error: " + e.getMessage());
        }
        return "redirect:/admin/enroll-students?courseId=" + courseId; // Redirect back to the enrollment form
    }


    @GetMapping("/admin/unenroll-student/{studentId}/{courseId}")
    public String unenrollStudent(@PathVariable Long studentId, @PathVariable Long courseId, RedirectAttributes redirectAttributes) {
        try {
            // Fetch the student and course
            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid student ID"));
            Course course = courseRepository.findByCourseId(courseId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));

            // Check if the student is already enrolled in the course
            Enrollment enrollment = enrollmentRepository.findByCourseAndStudent(course, student)
                    .orElseThrow(() -> new IllegalArgumentException("Student is not enrolled in this course"));

            // Unenroll the student
            enrollmentRepository.delete(enrollment);
            redirectAttributes.addFlashAttribute("success", "Student unenrolled successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error unenrolling student: " + e.getMessage());
        }

        return "redirect:/admin/enroll-students?courseId=" + courseId;  // Redirect back to the enrollment page
    }





}
