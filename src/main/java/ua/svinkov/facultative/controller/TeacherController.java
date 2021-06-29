package ua.svinkov.facultative.controller;

import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.svinkov.facultative.entity.Course;
import ua.svinkov.facultative.entity.UserCourses;
import ua.svinkov.facultative.service.CoursesService;
import ua.svinkov.facultative.service.UserService;
import ua.svinkov.facultative.util.ModelHelper;

@Controller
public class TeacherController {

	private final Logger log = LogManager.getLogger(StudentController.class);

	private final CoursesService coursesService;
	private final UserService userService;

	@Autowired
	public TeacherController(CoursesService coursesService, UserService userService) {
		this.userService = userService;
		this.coursesService = coursesService;
	}

	@GetMapping("/teacher")
	public String getTeacherPage(Model model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size, @RequestParam("optionSort") Optional<String> sort) {
		User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ua.svinkov.facultative.entity.User user = userService.findUserByLogin(userDetails.getUsername());
		Page<Course> courses = coursesService.findCoursesByTeacherId(user.getUserid(), page, size);
		log.trace("Found in DB: allCourses --> {}" + courses.get().collect(Collectors.toList()));

		model.addAttribute("allCourses", courses);
		ModelHelper.setPaginationAttributes(model, page, courses);
		return "teacherbasis";
	}

	@GetMapping("/coursestudents/{courseId}/course")
	public String getCourseStudentsPage(Model model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size, @PathVariable Long courseId) {
		Page<UserCourses> userCourses = coursesService.findStudentsByCourseId(courseId, page, size);
		model.addAttribute("students", userCourses);
		ModelHelper.setPaginationAttributes(model, page, userCourses);
		return "coursestudents";
	}

	@PostMapping("/setMark")
	public String setMark(Model model, @RequestParam("studentid") Optional<String> studentId,
			@RequestParam("courseid") Optional<String> courseId, @RequestParam("mark") Optional<Integer> mark) {
		UserCourses userCourse = coursesService.findAllByStudentAndCourseId(Long.parseLong(studentId.get()),
				Long.parseLong(courseId.get()));
		userCourse.setMark(mark.get());
		coursesService.create(userCourse);
		return "redirect:/coursestudents/" + Long.parseLong(courseId.get()) + "/course";
	}

}
