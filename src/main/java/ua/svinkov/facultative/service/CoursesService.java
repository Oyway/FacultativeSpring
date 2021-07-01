package ua.svinkov.facultative.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.svinkov.facultative.entity.Course;
import ua.svinkov.facultative.entity.Topic;
import ua.svinkov.facultative.entity.UserCourses;

/**
 * Interface for Courses service to manage courses
 * 
 * @author R. Svinkov
 *
 */
public interface CoursesService {
	/**
	 * Get list of all courses by student id
	 * 
	 * @param studentId Identity of student
	 * @return List of courses reg by student
	 */
	List<UserCourses> findAllByStudentId(Long studentId);

	/**
	 * Course by its id and student id
	 * 
	 * @param studentId Student identity
	 * @param courseId  Course identity
	 * @return Specific course registred by student
	 */
	UserCourses findAllByStudentAndCourseId(Long studentId, Long courseId);

	/**
	 * Get list of all courses
	 * 
	 * @return List of courses
	 */
	List<Course> findAllCourses();

	/**
	 * Get list of courses with pagination
	 * 
	 * @param page Pageable data for course
	 * @return List of course on defined page
	 */
	Page<Course> findAllCourses(Pageable page);

	/**
	 * Create entity
	 * 
	 * @param userCourses UserCourses entity
	 * @return updated UserCourses entity
	 */
	UserCourses create(UserCourses userCourses);

	/**
	 * Delete defined course
	 * 
	 * @param courseId Identity of deleted course
	 */
	void delete(Long courseId);

	/**
	 * Get all topics
	 * 
	 * @return List of topics
	 */
	List<Topic> findAllTopics();

	/**
	 * Create course
	 * 
	 * @param course Course entity
	 * @return updated course entity
	 */
	Course create(Course course);

	/**
	 * Find course by id
	 * 
	 * @param id Identity of course
	 * @return Optional of course
	 */
	Optional<Course> findCourseById(Long id);

	/**
	 * Update existed entity
	 * 
	 * @param course Course entity
	 * @return updated course
	 */
	Course updateCourse(Course course);

	/**
	 * Get course by defined page and identity of teacher
	 * 
	 * @param id   Identity of teacher
	 * @param page Page parameters
	 * @return Page of courses
	 */
	Page<Course> findCoursesByTeacherId(Long id, Pageable page);

	/**
	 * 
	 * Get UserCourse by defined page and identity of course
	 * 
	 * @param id   Identity of course
	 * @param page Page parameters
	 * @return Page of UserCourses
	 */
	Page<UserCourses> findStudentsByCourseId(Long id, Pageable page);

	/**
	 * Get page of course defined by page and surname of teacher
	 * 
	 * @param surname Surname of teacher
	 * @param page    Page parameters
	 * @return Page of course
	 */
	Page<Course> findAllByTeacherSurname(String surname, Pageable page);

	/**
	 * Get page of course defined by page and name of topic
	 * 
	 * @param topic Topic name
	 * @param page  Page parameters
	 * @return Page of course
	 */
	Page<Course> findAllByTopicTopic(String topic, Pageable page);

	/**
	 * Get page of course defined by page, name of topic and teacher surname
	 * 
	 * @param surname Surname of teacher
	 * @param topic   Topic name
	 * @param page    Page parameters
	 * @return Page of course
	 */
	Page<Course> findAllByTeacherSurnameAndTopicTopic(String surname, String topic, Pageable page);

}
