package ua.svinkov.facultative.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.svinkov.facultative.entity.Course;
import ua.svinkov.facultative.entity.UserCourses;
import ua.svinkov.facultative.service.CoursesService;
import ua.svinkov.facultative.service.UserService;
import ua.svinkov.facultative.util.ModelHelper;
import ua.svinkov.facultative.util.PageRequestHelper;

@Controller
public class StudentController {

	private final Logger log = LogManager.getLogger(StudentController.class);

	private final CoursesService coursesService;
	private final UserService userService;

	@Autowired
	public StudentController(CoursesService coursesService, UserService userService) {
		this.userService = userService;
		this.coursesService = coursesService;
	}

	@GetMapping("/student")
	public String getStudentPage(Model model) {
		User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ua.svinkov.facultative.entity.User user = userService.findUserByLogin(userDetails.getUsername());
		List<UserCourses> courses = coursesService.findAllByStudentId(user.getUserid());
		log.trace("Found in DB: user --> " + user);

		List<UserCourses> coursesNotStarted = courses.stream()
				.filter(t -> LocalDate.now().isBefore(t.getCourse().getDateStart())).collect(Collectors.toList());
		log.trace("Found in DB: coursesNotStarted --> " + coursesNotStarted);

		List<UserCourses> coursesActive = courses.stream()
				.filter(t -> LocalDate.now().isAfter(t.getCourse().getDateStart())
						&& LocalDate.now().isBefore(t.getCourse().getDateEnd()))
				.collect(Collectors.toList());
		log.trace("Found in DB: coursesActive --> " + coursesActive);

		List<UserCourses> coursesEnded = courses.stream()
				.filter(t -> LocalDate.now().isAfter(t.getCourse().getDateEnd())).collect(Collectors.toList());
		log.trace("Found in DB: coursesEnded --> " + coursesEnded);

		model.addAttribute("userCoursesNotStarted", coursesNotStarted);
		model.addAttribute("userCoursesActive", coursesActive);
		model.addAttribute("userCoursesEnded", coursesEnded);
		return "userbasis";
	}

	@GetMapping("/allcourses")
	public String getStudentAllCoursesPage(Model model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size, @RequestParam("optionSort") Optional<String> sort,
			@RequestParam("sortField") Optional<String> sortField, @RequestParam("sortDir") Optional<String> sortDir,
			@RequestParam("topic") Optional<String> topic, @RequestParam("teacher") Optional<String> teacher) {
		Page<Course> courses = null;
		boolean top = topic.isPresent() && !topic.get().equals("");
		boolean teach = teacher.isPresent() && !teacher.get().equals("");
		boolean fSort = sortField.isPresent() && !sortField.get().equals("");
		if (top && teach) {
			model.addAttribute("topic", topic.get());
			model.addAttribute("teacher", teacher.get());
			courses = coursesService.findAllByTeacherSurnameAndTopicTopic(teacher.get(), topic.get(),
					PageRequestHelper.createPageRequest(page, size, sortField, sortDir));
		} else if (top) {
			model.addAttribute("topic", topic.get());
			courses = coursesService.findAllByTopicTopic(topic.get(),
					PageRequestHelper.createPageRequest(page, size, sortField, sortDir));
		} else if (teach) {
			model.addAttribute("teacher", teacher.get());
			courses = coursesService.findAllByTeacherSurname(teacher.get(),
					PageRequestHelper.createPageRequest(page, size, sortField, sortDir));
		} else if (fSort) {
			courses = coursesService
					.findAllCourses(PageRequestHelper.createPageRequest(page, size, sortField, sortDir));
		} else {
			courses = coursesService.findAllCourses(page, size);
		}

		log.trace("{}", courses);

		model.addAttribute("allCourses", courses);

		if (fSort) {
			ModelHelper.setSortingPaginationAttributes(model, page, sortField, sortDir, courses);
		} else {
			ModelHelper.setPaginationAttributes(model, page, courses);
		}
		return "studentAllCourses";
	}

	@GetMapping("/profile")
	public String getProfilePage(Model model) {
		User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ua.svinkov.facultative.entity.User user = userService.findUserByLogin(userDetails.getUsername());
		log.trace("{}", user);
		model.addAttribute("login", user.getLogin());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("firstName", user.getFirstName());
		model.addAttribute("surname", user.getSurname());
		return "profile";
	}

	@PostMapping("/allcourses")
	public String postStudentAllCoursesPage(Model model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size, @RequestParam("optionSort") Optional<String> sort,
			@RequestParam("topic") Optional<String> topic, @RequestParam("teacher") Optional<String> teacher) {
		log.trace("Found: sort --> " + sort.get());
		Optional<String> sortField = Optional.of(sort.get().split(",")[0]);
		Optional<String> sortDir = Optional.of(sort.get().split(",")[1]);
		Page<Course> courses = null;
		boolean top = topic.isPresent() && !topic.get().equals("");
		boolean teach = teacher.isPresent() && !teacher.get().equals("");

		PageRequest p = PageRequestHelper.createPageRequest(page, size, sortField, sortDir);

		if (top && teach) {
			model.addAttribute("topic", topic.get());
			model.addAttribute("teacher", teacher.get());
			courses = coursesService.findAllByTeacherSurnameAndTopicTopic(teacher.get(), topic.get(), p);
		} else if (top) {
			model.addAttribute("topic", topic.get());
			courses = coursesService.findAllByTopicTopic(topic.get(), p);
		} else if (teach) {
			model.addAttribute("teacher", teacher.get());
			courses = coursesService.findAllByTeacherSurname(teacher.get(), p);
		} else {
			courses = coursesService.findAllCourses(p);
		}

		model.addAttribute("allCourses", courses);

		ModelHelper.setSortingPaginationAttributes(model, page, sortField, sortDir, courses);
		return "studentAllCourses";

	}

	@PostMapping("/reg_course")
	public String regOnCourses(Model model, @RequestParam("option") Optional<String[]> id) {
		String forward = "redirect:/allcourses";
		User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		StringBuilder regMessage = new StringBuilder();

		if (id.isEmpty())
			return forward;

		log.trace("{}", (Object) id.get());

		for (int i = 0; i < id.get().length; i++) {
			UserCourses courses = new UserCourses();
			courses.setUser(userService.findUserByLogin(userDetails.getUsername()));
			courses.setCourse(Course.builder().courseid(Long.parseLong(id.get()[i])).build());
			if (Objects.isNull(coursesService.findAllByStudentAndCourseId(courses.getUser().getUserid(),
					Long.parseLong(id.get()[i])))) {
				log.trace("Insert in DB: users_courses --> {} " + courses);
				coursesService.create(courses);
				log.trace("Insert in DB: users_courses --> {} " + courses);
				regMessage.append("Complete reg to course ").append(Integer.parseInt(id.get()[i])).append(" ");
			} else {
				regMessage.append("Already registred to course ").append(Integer.parseInt(id.get()[i])).append(" ");
			}
		}
		log.trace("{} " + regMessage);
		model.addAttribute("regMessage", regMessage);
		return forward;
	}
}
