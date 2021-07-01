package ua.svinkov.facultative.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ua.svinkov.facultative.entity.UserCourses;

public interface CoursesRepository extends JpaRepository<UserCourses, Long> {
	
	List<UserCourses> findAllByUserId(Long studentId);

	UserCourses findAllByUserIdAndCourseId(Long studentId, Long courseId);

	Page<UserCourses> findAllByCourseId(Long id, Pageable pageable);
}
