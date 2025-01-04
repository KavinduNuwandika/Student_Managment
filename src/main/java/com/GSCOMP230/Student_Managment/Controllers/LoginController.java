package com.GSCOMP230.Student_Managment.Controllers;

import com.GSCOMP230.Student_Managment.model.User;
import com.GSCOMP230.Student_Managment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String showLoginPage() {
        return "Login"; // Return login page
    }



    @GetMapping("/teacher/home")
    public String showTeacherDashboard(HttpSession session, Model model) {
        String email = (String) session.getAttribute("userEmail");
        if (email != null) {
            Optional<User> userOpt = userRepository.findByEmail(email); // Fetch user by email
            userOpt.ifPresent(user -> model.addAttribute("user", user)); // Pass user details to the dashboard
        }
        return "Teacher/TeacherHome"; // Return the teacher dashboard view
    }

    @GetMapping("/student/home")
    public String showStudentResults(HttpSession session, Model model) {
        String email = (String) session.getAttribute("userEmail");
        if (email != null) {
            Optional<User> userOpt = userRepository.findByEmail(email); // Fetch user by email
            userOpt.ifPresent(user -> model.addAttribute("user", user)); // Pass user details to the results page
        }
        return "Student/StudentHome"; // Return the student results view
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam("identifier") String identifier, // Can be email or phone
            @RequestParam("password") String password,
            Model model,
            HttpSession session) {

        // Try to find the user by email and password, then by phone and password
        User user = userRepository.findByEmailAndPassword(identifier, password);

        if (user == null) {
            user = userRepository.findByPhoneAndPassword(identifier, password); // Check phone if email fails
        }

        if (user != null) {
            // Store the user's email in the session
            session.setAttribute("userEmail", user.getEmail());

            // If the user is a teacher, store the teacherId in the session
            if (user.getRole().equals("Teacher")) {
                session.setAttribute("teacherId", user.getId()); // Store teacher's ID in session
            }

            switch (user.getRole()) {
                case "Admin":
                    return "redirect:/admin/home"; // Redirect to admin home page
                case "Teacher":
                    return "redirect:/teacher/home"; // Redirect to teacher dashboard
                case "Student":
                    return "redirect:/student/home"; // Redirect to student results page
                default:
                    model.addAttribute("error", "Invalid role configuration.");
                    return "LoginError"; // Stay on login page with an error message
            }
        } else {
            model.addAttribute("error", "Invalid credentials. Please try again.");
            return "LoginError"; // Stay on login page with an error message
        }
    }
}
