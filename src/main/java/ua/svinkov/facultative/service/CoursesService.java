package ua.svinkov.facultative.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import ua.svinkov.facultative.entity.Course;
import ua.svinkov.facultative.entity.Topic;
import ua.svinkov.facultative.entity.UserCourses;

public interface CoursesService {
	List<UserCourses> findAllByStudentId(Long studentId);

	UserCourses findAllByStudentAndCourseId(Long studentId, Long courseId);

	List<Course> findAllCourses();
	
	Page<Course> findAllCourses(PageRequest page);

	UserCourses create(UserCourses userCourses);

	Page<Course> findAllCourses(Optional<Integer> page, Optional<Integer> size);

	void delete(Long courseId);

	List<Topic> findAllTopics();

	Course create(Course course);

	Optional<Course> findCourseById(Long id);

	Course updateCourse(Course course);

	Page<Course> findCoursesByTeacherId(Long id, Optional<Integer> page, Optional<Integer> size);

	Page<UserCourses> findStudentsByCourseId(Long id, Optional<Integer> page, Optional<Integer> size);
	
	Page<Course> findAllByTeacherSurname(String surname, Pageable page);
	
	Page<Course> findAllByTopicTopic(String topic, Pageable page);
	
	Page<Course> findAllByTeacherSurnameAndTopicTopic(String surname, String topic, Pageable page);

}
