package ua.svinkov.facultative.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import ua.svinkov.facultative.entity.Course;

public interface CourseSortRepository extends PagingAndSortingRepository<Course,Long> {
	
	Page<Course> findAll(Pageable page);
	
	Page<Course> findAllByTeacherSurname(String surname, Pageable page);
	
	Page<Course> findAllByTopicTopic(String topic, Pageable page);
	
	Page<Course> findAllByTeacherSurnameAndTopicTopic(String surname, String topic, Pageable page);

}
