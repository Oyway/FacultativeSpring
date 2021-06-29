package ua.svinkov.facultative.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ua.svinkov.facultative.entity.Role;
import ua.svinkov.facultative.entity.User;
import ua.svinkov.facultative.service.PasswordService;
import ua.svinkov.facultative.service.UserService;

@Slf4j
@Controller
public class RegFormController {

	private final UserService userService;
	private final PasswordService passwordService;

	@Autowired
	public RegFormController(UserService userService, PasswordService passwordService) {
		this.userService = userService;
		this.passwordService = passwordService;
	}

	@RequestMapping("/reg")
	public String regForm(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("error", null);
		return "reg";
	}

	@PostMapping("/registration")
	public String regUser(@ModelAttribute User user, Model model) {
		log.info("{}", user);
		User foundUser = userService.findUserByLogin(user.getLogin());

		if (foundUser != null) {
			model.addAttribute("error", "User already exists");
			return "reg";
		}
		user.setPassword(passwordService.encodePassword(user.getPassword()));
		user.setRole(new Role(2L, "STUDENT"));
		user.setStatus(true);
		userService.create(user);
		return "redirect:/login";
	}
}
