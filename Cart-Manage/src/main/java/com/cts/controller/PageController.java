package com.cts.controller;

import com.cts.entity.User;
import com.cts.service.UserService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String toLoginPage() {
        return "redirect:/showLogin";
    }

    @GetMapping("/showLogin")
    public String showLoginPage() {
        return "slidingsignin";
    }

    @GetMapping("/showHome")
    public String showHomePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUsername", authentication.getName());
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        return "home";
    }

    @GetMapping("/showProfile")
    public String showProfilePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Optional<User> userOptional = userService.findByUsername(currentUsername);

        model.addAttribute("user", userOptional.orElse(new User()));
        return "profile";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User updatedUser = userService.updateUserProfile(currentUsername, user);
        if (updatedUser != null) {
            redirectAttributes.addFlashAttribute("message", "Profile updated Successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Error: User not found for update");
        }
        return "redirect:/showProfile";
    }

    @PostMapping("/registerUser")
    @ResponseBody
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody User user, BindingResult result) {
        Map<String, String> response = new HashMap<>();

        if (result.hasErrors()) {
            response.put("status", "error");
            response.put("message", result.getFieldError().getDefaultMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (userService.findByUsername(user.getUsername()).isPresent()) {
            response.put("status", "error");
            response.put("message", "Username is already taken");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } else if (userService.findByEmail(user.getEmail()).isPresent()) {
            response.put("status", "error");
            response.put("message", "Email is already taken");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } else {
            userService.saveUser(user);
            response.put("status", "success");
            response.put("message", "Registration successful! Please login");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
