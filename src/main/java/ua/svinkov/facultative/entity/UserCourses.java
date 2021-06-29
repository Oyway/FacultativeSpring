package ua.svinkov.facultative.entity;

import java.io.Serializable;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * UserCourses entity
 * 
 * @author R.Svinkov
 *
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users_courses", uniqueConstraints = @UniqueConstraint(columnNames = { "student_id",
		"course_id" }, name = "STUDENT_COURSES_UNIQUE_CONSTRAINT"))
public class UserCourses implements Serializable {

	private static final long serialVersionUID = 6299005808254740364L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "student_id", nullable = false, foreignKey = @ForeignKey(name = "FK_STUDENT"))
	private User user;

	@ManyToOne
	@JoinColumn(name = "course_id", nullable = false, foreignKey = @ForeignKey(name = "FK_COURSE"))
	private Course course;

	@Column
	private Integer mark;
}
