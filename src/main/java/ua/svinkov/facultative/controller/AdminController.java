package ua.svinkov.facultative.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.svinkov.facultative.entity.Course;
import ua.svinkov.facultative.entity.Role;
import ua.svinkov.facultative.entity.Topic;
import ua.svinkov.facultative.entity.User;
import ua.svinkov.facultative.service.CoursesService;
import ua.svinkov.facultative.service.UserService;
import ua.svinkov.facultative.util.ModelHelper;
import ua.svinkov.facultative.util.PageRequestHelper;

@Controller
public class AdminController {

	private final Logger log = LogManager.getLogger(AdminController.class);
	private final CoursesService coursesService;
	private final UserService userService;

	@Autowired
	@Resource(name = "sessionRegistry")
	private SessionRegistry sessionRegistry;

	private static final String PARAM_OPTION_TOPICS = "optionTopics";
	private static final String PARAM_COURSE = "course";
	private static final String PARAM_OPTION_TEACHER = "optionTeacher";
	private static final String PARAM_DATE_START = "dateStart";
	private static final String PARAM_DATE_END = "dateEnd";
	private static final String PARAM_DESCRIPTION = "description";

	private static final String PARAM_COURSE_ID = "courseId";
	private static final String PARAM_COURSE_NAME = "courseName";

	@Autowired
	public AdminController(CoursesService coursesService, UserService userService, SessionRegistry sessionRegistry) {
		this.userService = userService;
		this.coursesService = coursesService;
		this.sessionRegistry = sessionRegistry;
	}

	@GetMapping("/admin")
	public String getStudentPage(Model model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {
		Page<Course> courses = coursesService
				.findAllCourses(PageRequestHelper.createPageRequest(page, size, Optional.empty(), Optional.empty()));
		List<Topic> topics = coursesService.findAllTopics();
		List<User> teachers = userService.findAllByRole(3L);
		log.trace("Found in DB: allCourses --> {}" + courses.get().collect(Collectors.toList()));
		model.addAttribute("allCourses", courses);
		model.addAttribute("allTopics", topics);
		model.addAttribute("allTeachers", teachers);
		ModelHelper.setPaginationAttributes(model, page, courses);
		return "adminbasis";
	}

	@PostMapping("/{courseId}/delete")
	public String deleteCourse(Model model, @PathVariable Long courseId, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {
		coursesService.delete(courseId);
		Page<Course> courses = coursesService
				.findAllCourses(PageRequestHelper.createPageRequest(page, size, Optional.empty(), Optional.empty()));
		log.trace("Found in DB: allCourses --> {}" + courses.get().collect(Collectors.toList()));
		model.addAttribute("allCourses", courses);
		ModelHelper.setPaginationAttributes(model, page, courses);
		return "redirect:/admin";
	}

	@PostMapping("/addCourse")
	public String addCourse(Model model, HttpServletRequest request) {
		String course = request.getParameter(PARAM_COURSE);
		Long topicId = Long.parseLong(request.getParameter(PARAM_OPTION_TOPICS));
		Long teacher = Long.parseLong(request.getParameter(PARAM_OPTION_TEACHER));
		LocalDate dateStart = LocalDate.parse(request.getParameter(PARAM_DATE_START));
		LocalDate dateEnd = LocalDate.parse(request.getParameter(PARAM_DATE_END));
		String description = request.getParameter(PARAM_DESCRIPTION);

		Course newCourse = Course.builder().course(course).topic(Topic.builder().id(topicId).build())
				.teacher(User.builder().id(teacher).build()).dateStart(dateStart).dateEnd(dateEnd)
				.description(description).build();
		coursesService.create(newCourse);
		return "redirect:/admin";
	}

	@PostMapping("/courseedit/{courseId}/edit")
	public String editCoursePage(Model model, @PathVariable Long courseId) {
		Course course = coursesService.findCourseById(courseId).get();

		log.trace("Found in DB: allCourses --> {}" + course);
		List<Topic> topics = coursesService.findAllTopics();
		List<User> teachers = userService.findAllByRole(3L);

		model.addAttribute("allTopics", topics);
		model.addAttribute("allTeachers", teachers);
		model.addAttribute(PARAM_COURSE_ID, course.getId());
		model.addAttribute(PARAM_COURSE_NAME, course.getCourse());
		model.addAttribute("currentId", course.getTopic().getId());
		model.addAttribute(PARAM_OPTION_TEACHER, course.getTeacher().getId());
		model.addAttribute(PARAM_DATE_START, course.getDateStart());
		model.addAttribute(PARAM_DATE_END, course.getDateEnd());
		model.addAttribute(PARAM_DESCRIPTION, course.getDescription());
		return "courseedit";
	}

	@PostMapping("/editCourse")
	public String editCourse(Model model, HttpServletRequest request) {
		Course course = Course.builder().id(Long.parseLong(request.getParameter(PARAM_COURSE_ID)))
				.course(request.getParameter(PARAM_COURSE_NAME))
				.topic(Topic.builder().id(Long.parseLong(request.getParameter(PARAM_OPTION_TOPICS))).build())
				.teacher(User.builder().id(Long.parseLong(request.getParameter(PARAM_OPTION_TEACHER))).build())
				.dateStart(LocalDate.parse(request.getParameter(PARAM_DATE_START)))
				.dateEnd(LocalDate.parse(request.getParameter(PARAM_DATE_END)))
				.description(request.getParameter(PARAM_DESCRIPTION)).build();
		coursesService.updateCourse(course);
		return "redirect:/admin";
	}

	@GetMapping("/users")
	public String getUsersPage(Model model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {
		Page<User> users = userService
				.findAllUsers(PageRequestHelper.createPageRequest(page, size, Optional.empty(), Optional.empty()));
		log.trace("Found in DB: allCourses --> {}" + users.get().collect(Collectors.toList()));
		model.addAttribute("allUsers", users);
		model.addAttribute("allRoles", userService.findAllRoles());
		ModelHelper.setPaginationAttributes(model, page, users);
		return "users";
	}

	@PostMapping("/users/{userId}/block")
	public String blockUser(Model model, @PathVariable Long userId) {
		User blockUser = userService.findUserById(userId).get();
		blockUser.setStatus(false);
		userService.create(blockUser);
		stopSession(blockUser);
		return "redirect:/users";
	}

	@PostMapping("/users/{userId}/unblock")
	public String unblockUser(Model model, @PathVariable Long userId) {
		User blockUser = userService.findUserById(userId).get();
		blockUser.setStatus(true);
		userService.create(blockUser);
		stopSession(blockUser);
		return "redirect:/users";
	}

	@PostMapping("/users/{userId}/update")
	public String updatekUserRole(Model model, @PathVariable Long userId,
			@RequestParam("optionRoles") Optional<Role> role) {
		User updateUser = userService.findUserById(userId).get();
		log.trace("Role - > ", role.get());
		updateUser.setRole(role.get());
		userService.create(updateUser);
		stopSession(updateUser);
		return "redirect:/users";
	}
	
	
	private void stopSession(User stopUser) {
		final List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
		for (final Object principal : allPrincipals) {
			if (principal instanceof org.springframework.security.core.userdetails.User) {
				final org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) principal;
				if (stopUser.getLogin().equals(user.getUsername())) {
					for (SessionInformation information : sessionRegistry.getAllSessions(user, true)) {
						information.expireNow();
					}
				}
			}
		}
	}
}
