package com.GSCOMP230.Student_Managment.Controllers;

import com.GSCOMP230.Student_Managment.model.User;
import com.GSCOMP230.Student_Managment.repository.UserRepository;
import com.GSCOMP230.Student_Managment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String homePage() {
        return "HomePage";
    }

    @GetMapping("/Homepage")
    public String homepage() {
        return "homepage"; // Return homepage view
    }


    @GetMapping("/SignUp")
    public String dataEntryPage(Model model) {
        model.addAttribute("user", new User());
        return "SignUp";
    }
    @PostMapping("/SignUp")
    public String signUp(@ModelAttribute("user") User user, Model model) {
        // Check if email or phone already exists
        if (userService.isEmailOrPhoneExist(user.getEmail(), user.getPhone())) {
            model.addAttribute("error", "Email or Phone number already exists!");
            return "SignUp"; // Return to the same page with the error
        }

        // Save the user if no error
        userService.saveUser(user);
        return "redirect:/"; // Redirect to login after successful signup
    }

    @GetMapping("/loginPage")
    public String loginPage() {
        return "Login"; // Return login page view
    }

    @PostMapping("/submit")
    public String submitForm(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/";
    }

    @GetMapping("/change-details")
    public String changeDetailsPage(@RequestParam String email, Model model) {
        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
        model.addAttribute("user", user);  // Pass the user object to the template
        return "change-details";  // The name of your HTML file
    }


    @PostMapping("/changedetails")
    public String updateUserDetails(@ModelAttribute User user, Model model) {
        User existingUser = userService.getUserByEmail(user.getEmail());
        if (existingUser == null) {
            model.addAttribute("error", "User not found");
            return "error";
        }

        // Update user details
        existingUser.setFirstName(user.getFirstName() != null ? user.getFirstName() : existingUser.getFirstName());
        existingUser.setLastName(user.getLastName() != null ? user.getLastName() : existingUser.getLastName());
        existingUser.setPhone(user.getPhone() != null ? user.getPhone() : existingUser.getPhone());
        existingUser.setDob(user.getDob() != null ? user.getDob() : existingUser.getDob());
        existingUser.setRegYear(user.getRegYear() != 0 ? user.getRegYear() : existingUser.getRegYear());

        // Save the updated user
        userService.updateUser(existingUser);

        // Add the updated user to the model
        model.addAttribute("user", existingUser);  // Important! This ensures the user object is available in the view.
        model.addAttribute("success", "Details updated successfully");

        return "details-update-success"; // Thymeleaf template name
    }




    @GetMapping("/change-password")
    public String changePasswordPage(@RequestParam String email, Model model) {
        // Fetch user details by email
        User user = userService.getUserByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found"); // Handle user not found
        }

        // Add the user to the model
        model.addAttribute("user", user);
        return "change-password"; // Thymeleaf template name
    }


    @PostMapping("/changepassword")
    public String changePassword(@RequestParam String email,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 Model model) {
        // Fetch the user by email
        User user = userService.getUserByEmail(email);
        if (user == null) {
            model.addAttribute("error", "User not found");
            return "error"; // Or handle error page accordingly
        }

        // Check if the old password matches
        if (!user.getPassword().equals(oldPassword)) {
            model.addAttribute("error", "Old password is incorrect");
            return "error"; // Or handle error page accordingly
        }

        // Update the password
        user.setPassword(newPassword);
        userService.updateUser(user);

        // Add success message and return the success page
        model.addAttribute("successMessage", "Password updated successfully");
        return "password-update-success"; // Thymeleaf template name
    }






}
