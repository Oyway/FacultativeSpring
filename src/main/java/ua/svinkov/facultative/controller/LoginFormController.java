package ua.svinkov.facultative.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.svinkov.facultative.entity.User;
import ua.svinkov.facultative.service.PasswordService;
import ua.svinkov.facultative.service.UserService;

@Controller
public class LoginFormController {

	private final UserService userService;
	private final PasswordService passwordService;

	@Autowired
	public LoginFormController(UserService userService, PasswordService passwordService) {
		this.userService = userService;
		this.passwordService = passwordService;
	}

	@GetMapping("/welcome")
	public String getMainPage() {
		return "index";
	}

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("error", null);
		return "login";
	}

	@GetMapping("/login/error")
	public String loginUser(@ModelAttribute User user, Model model) {
		String path = "login";
		User foundUser = userService.findUserByLogin(user.getLogin());

		if (foundUser == null) {
			model.addAttribute("error", "User doesn't exists or account blocked");
			return path;
		}

		if (!passwordService.compareRawAndEncodedPassword(user.getPassword(), (foundUser.getPassword()))) {
			model.addAttribute("error", "Wrong user/password");
			return path;
		}
		return path;
	}
}
