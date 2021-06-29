package ua.svinkov.facultative.entity;

import java.io.Serializable;

import java.time.LocalDate;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Course entity
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
@Entity(name = "courses")
@Table
public class Course implements Serializable {

	private static final long serialVersionUID = -1334870106776022108L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long courseid;

	@Column(nullable = false)
	private String course;

	@ManyToOne
	@JoinColumn(name = "topic_id", nullable = false, foreignKey = @ForeignKey(name = "FK_TOPIC"))
	private Topic topic;

	@ManyToOne
	@JoinColumn(name = "teacher_id", nullable = false, foreignKey = @ForeignKey(name = "FK_TEACHER"))
	private User teacher;

	@Column(nullable = false)
	private LocalDate dateStart;

	@Column(nullable = false)
	private LocalDate dateEnd;

	@Column(nullable = false)
	private String description;
}
