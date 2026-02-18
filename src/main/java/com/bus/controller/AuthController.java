package com.bus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.bus.model.User;
import com.bus.service.AuthService;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	// ================= REGISTER =================
	@GetMapping("/register")
	public String registerPage(Model model) {
		model.addAttribute("user", new User());
		return "users/register";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute User user,
			RedirectAttributes redirectAttributes) {

		authService.register(user);

		redirectAttributes.addFlashAttribute(
				"message",
				"Registration successful! Please login.");

		return "redirect:/auth/login-user";
	}

	// ================= LOGIN PAGES =================
	@GetMapping("/login-admin")
	public String loginAdminPage() {
		return "users/login-admin";
	}

	@GetMapping("/login-user")
	public String loginUserPage() {
		return "users/login-user";
	}

	// ================= LOGIN =================
	@PostMapping("/login")
	public String login(@RequestParam String username,
			@RequestParam String password,
			@RequestParam String role,
			HttpSession session,
			RedirectAttributes redirectAttributes) {

		// -------- ADMIN LOGIN --------
		if ("admin".equalsIgnoreCase(role)) {

			if ("admin".equals(username) && "admin123".equals(password)) {

				session.setAttribute("admin", username);

				redirectAttributes.addFlashAttribute(
						"message",
						"Admin login successful!");

				return "redirect:/admin";
			}

			redirectAttributes.addFlashAttribute(
					"error",
					"Invalid admin credentials");

			return "redirect:/auth/login-admin";
		}

		// -------- USER LOGIN --------
		User u = authService.loginUser(username, password);

		if (u != null) {

			session.setAttribute("user", u);

			redirectAttributes.addFlashAttribute(
					"message",
					"Welcome back, " + u.getFirstName() + "!");

			return "redirect:/";
		}

		redirectAttributes.addFlashAttribute(
				"error",
				"Invalid username or password");

		return "redirect:/auth/login-user";
	}

	// ================= LOGOUT =================
	@GetMapping("/logout")
	public String logout(HttpSession session,
			RedirectAttributes redirectAttributes) {

		session.invalidate();

		redirectAttributes.addFlashAttribute(
				"message",
				"Logged out successfully");

		return "redirect:/";
	}
}
