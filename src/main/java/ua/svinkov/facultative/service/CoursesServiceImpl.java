package ua.svinkov.facultative.service;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ua.svinkov.facultative.entity.Course;
import ua.svinkov.facultative.entity.Topic;
import ua.svinkov.facultative.entity.UserCourses;
import ua.svinkov.facultative.repository.CourseRepository;
import ua.svinkov.facultative.repository.CourseSortRepository;
import ua.svinkov.facultative.repository.CoursesRepository;
import ua.svinkov.facultative.repository.TopicRepository;

/**
 * Implementation of course service for manage courses
 * 
 * @author R. Svinkov
 *
 */
@Service
public class CoursesServiceImpl implements CoursesService {

	private final CoursesRepository coursesRepository;
	private final CourseRepository courseRepository;
	private final TopicRepository topicRepository;
	private final CourseSortRepository courseSortRepository;

	public CoursesServiceImpl(CoursesRepository coursesRepository, CourseRepository courseRepository,
			TopicRepository topicRepository, CourseSortRepository courseSortRepository) {
		this.coursesRepository = coursesRepository;
		this.courseRepository = courseRepository;
		this.topicRepository = topicRepository;
		this.courseSortRepository = courseSortRepository;
	}

	@Override
	public List<UserCourses> findAllByStudentId(Long studentId) {
		return coursesRepository.findAllByUserId(studentId);
	}

	@Override
	public List<Course> findAllCourses() {
		return courseRepository.findAll();
	}

	@Override
	public UserCourses create(UserCourses userCourses) {
		requireNonNull(userCourses, "userCourses must be not null");
		return coursesRepository.save(userCourses);
	}

	@Override
	public UserCourses findAllByStudentAndCourseId(Long studentId, Long courseId) {
		return coursesRepository.findAllByUserIdAndCourseId(studentId, courseId);
	}

	@Override
	public Page<Course> findAllCourses(Pageable page) {
		return courseRepository.findAll(page);
	}

	@Override
	public void delete(Long courseId) {
		courseRepository.deleteById(courseId);
		;
	}

	@Override
	public List<Topic> findAllTopics() {
		return topicRepository.findAll();
	}

	@Override
	public Course create(Course course) {
		return courseRepository.save(course);
	}

	@Override
	public Optional<Course> findCourseById(Long id) {
		return courseRepository.findById(id);
	}

	@Override
	public Course updateCourse(Course course) {
		return courseRepository.save(course);
	}

	@Override
	public Page<Course> findCoursesByTeacherId(Long id, Pageable page) {
		return courseRepository.findAllByTeacherId(id, page);
	}

	@Override
	public Page<UserCourses> findStudentsByCourseId(Long id, Pageable page) {
		return coursesRepository.findAllByCourseId(id, page);
	}

	@Override
	public Page<Course> findAllByTeacherSurname(String surname, Pageable page) {
		return courseSortRepository.findAllByTeacherSurname(surname, page);
	}

	@Override
	public Page<Course> findAllByTopicTopic(String topic, Pageable page) {
		return courseSortRepository.findAllByTopicTopic(topic, page);
	}

	@Override
	public Page<Course> findAllByTeacherSurnameAndTopicTopic(String surname, String topic, Pageable page) {
		return courseSortRepository.findAllByTeacherSurnameAndTopicTopic(surname, topic, page);
	}

}
