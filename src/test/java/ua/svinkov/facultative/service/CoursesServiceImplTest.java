package ua.svinkov.facultative.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ua.svinkov.facultative.repository.CourseRepository;
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
    private TopicRepository topicRep;

    @Test
    void findAllByStudentAndCourseId() {
        service.findAllByStudentAndCourseId(1L, 1L);
        verify(coursesRep, times(1)).findAllByUserIdAndCourseId(1L, 1L);
    }
    
    @Test
    void findAllByStudentId() {
        service.findAllByStudentId(1L);
        verify(coursesRep, times(1)).findAllByUserId(1L);
    }

    @Test
    void findAllCourses() {
        service.findAllCourses();
        verify(courseRep, times(1)).findAll();
    }

}
