package ua.svinkov.facultative.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import ua.svinkov.facultative.entity.Course;
import ua.svinkov.facultative.entity.Topic;
import ua.svinkov.facultative.entity.User;
import ua.svinkov.facultative.entity.UserCourses;
import ua.svinkov.facultative.repository.CourseRepository;
import ua.svinkov.facultative.repository.CourseSortRepository;
import ua.svinkov.facultative.repository.CoursesRepository;
import ua.svinkov.facultative.repository.TopicRepository;

@ExtendWith(MockitoExtension.class)
class CoursesServiceImplTest {

	@InjectMocks
	private CoursesServiceImpl service;

	@Mock
	private CourseRepository courseRep;

	@Mock
	private CoursesRepository coursesRep;

	@Mock
	private CourseSortRepository courseSortRep;

	@Mock
	private TopicRepository topicRep;

	@Test
	void testFindAllByStudentId() {
		service.findAllByStudentId(1L);
		verify(coursesRep, times(1)).findAllByUserId(1L);
	}

	@Test
	void testFindAllCourses() {
		service.findAllCourses();
		verify(courseRep, times(1)).findAll();
	}

	@Test
	void testCreateUserCourses() {
		UserCourses userCourses = new UserCourses();
		userCourses.setId(1L);
		userCourses.setCourse(new Course());
		userCourses.setUser(new User());

		when(coursesRep.save(userCourses)).thenReturn(userCourses);

		service.create(userCourses);

		verify(coursesRep, times(1)).save(userCourses);
	}

	@Test
	void testFindAllByStudentAndCourseId() {
		service.findAllByStudentAndCourseId(1L, 1L);
		verify(coursesRep, times(1)).findAllByUserIdAndCourseId(1L, 1L);
	}

	@Test
	void testFindAllCoursesPageable() {
		PageRequest pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.unsorted());
		List<Course> course = Collections.emptyList();
		PageImpl<Course> response = new PageImpl<>(course, pageable, 1);
		when(courseRep.findAll(pageable)).thenReturn(response);

		service.findAllCourses(pageable);

		verify(courseRep, times(1)).findAll(pageable);
	}

	@Test
	void testDelete() {
		service.delete(1L);

		verify(courseRep, times(1)).deleteById(1L);
		;
	}

	@Test
	void testFindAllTopics() {
		List<Topic> topics = Collections.emptyList();
		when(topicRep.findAll()).thenReturn(topics);

		service.findAllTopics();

		verify(topicRep, times(1)).findAll();
	}

	@Test
	void testCreateCourse() {
		Course course = new Course();
		course.setId(20L);

		when(courseRep.save(course)).thenReturn(course);

		service.create(course);

		verify(courseRep, times(1)).save(course);
	}

	@Test
	void testFindCourseById() {
		Optional<Course> course = Optional.of(Course.builder().id(1L).build());
		when(courseRep.findById(1L)).thenReturn(course);

		service.findCourseById(1L);

		verify(courseRep, times(1)).findById(1L);
	}

	@Test
	void testUpdateCourse() {
		Course course = new Course();
		course.setId(20L);

		when(courseRep.save(course)).thenReturn(course);

		service.create(course);

		verify(courseRep, times(1)).save(course);
	}

	@Test
	void testFindCoursesByTeacherId() {
		PageRequest pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.unsorted());
		service.findCoursesByTeacherId(1L, pageable);
		verify(courseRep, times(1)).findAllByTeacherId(1L, pageable);
	}

	@Test
	void testFindStudentsByCourseId() {
		PageRequest pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.unsorted());
		service.findStudentsByCourseId(1L, pageable);
		verify(coursesRep, times(1)).findAllByCourseId(1L, pageable);
	}

	@Test
	void testFindAllByTeacherSurname() {
		PageRequest pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.unsorted());
		service.findAllByTeacherSurname("Surname", pageable);
		verify(courseSortRep, times(1)).findAllByTeacherSurname("Surname", pageable);
	}

	@Test
	void testFindAllByTopicTopic() {
		PageRequest pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.unsorted());
		service.findAllByTopicTopic("Topic", pageable);
		verify(courseSortRep, times(1)).findAllByTopicTopic("Topic", pageable);
	}

	@Test
	void testFindAllByTeacherSurnameAndTopicTopic() {
		PageRequest pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.unsorted());
		service.findAllByTeacherSurnameAndTopicTopic("Teacher", "Topic", pageable);
		verify(courseSortRep, times(1)).findAllByTeacherSurnameAndTopicTopic("Teacher", "Topic", pageable);
	}

}
