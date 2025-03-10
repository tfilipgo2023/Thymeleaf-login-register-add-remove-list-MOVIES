package com.website.thymeleaf.web.controller;

import com.website.thymeleaf.web.model.Movie;
import com.website.thymeleaf.web.model.Role;
import com.website.thymeleaf.web.model.User;
import com.website.thymeleaf.web.repository.RoleRepository;
import com.website.thymeleaf.web.repository.UserRepository;
import com.website.thymeleaf.web.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.stereotype.Controller
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "Email já está em uso!");
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = new Role();
            role.setName("ROLE_USER");
            roleRepository.save(role);
        }
        user.setRoles(List.of(role));

        userRepository.save(user);
        model.addAttribute("success", "Registado com sucesso!");
        return "register-success";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        System.out.println("Página de login exibida.");
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password) {
        System.out.println("Requisição de login recebida.");
        System.out.println("Email: " + username);
        System.out.println("Senha: " + password);
        return "redirect:/movies/lobby";
    }

    @GetMapping("/index")
    public String showMenuPrincipal() {
        return "index";
    }

}