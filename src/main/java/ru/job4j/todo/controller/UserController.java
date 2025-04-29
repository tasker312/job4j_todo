package ru.job4j.todo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.user.UserService;

import java.util.TimeZone;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String getLoginPage() {
        return "/users/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute User user, Model model, HttpServletRequest request) {
        var userOptional = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "Invalid login or password");
            model.addAttribute("user", new User(0, "Гость", "", "", ""));
            return "/users/login";
        }
        request.getSession().setAttribute("user", userOptional.get());
        return "redirect:/tasks";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

    @GetMapping("/register")
    public String getRegisterPage(TimeZone timeZone, Model model) {
        var zones = userService.getTimeZones();
        model.addAttribute("timeZone", timeZone.toZoneId().toString());
        model.addAttribute("zones", zones);
        return "/users/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute User user, Model model, HttpServletRequest request) {
        var userOptional = userService.save(user);
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "User with login '%s' already exists".formatted(user.getLogin()));
            model.addAttribute("user", new User(0, "Гость", "", "", ""));
            return "/users/register";
        }
        request.getSession().setAttribute("user", userOptional.get());
        return "redirect:/users/login";
    }

}
