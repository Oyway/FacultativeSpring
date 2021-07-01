package ua.svinkov.facultative.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.svinkov.facultative.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

	Page<Course> findAllByTeacherId(Long clientId, Pageable pageable);
	
	

}
