package lv.venta.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "Lesson")
@Entity
public class Lesson {

	@Id
	@Column(name = "LessonId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(value = AccessLevel.NONE)
	private int lessonId;

	@ManyToOne
	@JoinColumn(name = "CourseId")
	@ToString.Exclude
	private Course course;

	@OneToMany(mappedBy = "lesson")
	@ToString.Exclude
	@JsonIgnore
	private ArrayList<LessonDateTime> lessonTimes;

	@ManyToOne
	@JoinColumn(name = "UserId")
	@ToString.Exclude
	private Lecturer lecturer;

	@ManyToOne
	@JoinColumn(name = "ClassroomId")
	@ToString.Exclude
	private Classroom classroom;

	@ManyToOne
	@JoinColumn(name = "SemesterId")
	@ToString.Exclude
	private Semester semester;

	private int lessonGroup;

	private boolean isOnline;

	private String onlineInformation;

	public Lesson(Course course, Lecturer lecturer, Classroom classroom, Semester semester, int lessonGroup,
			boolean isOnline, String onlineInformation) {
		setCourse(course);
		setLecturer(lecturer);
		setClassroom(classroom);
		setSemester(semester);
		setLessonGroup(lessonGroup);
		setOnline(isOnline);
		setOnlineInformation(onlineInformation);
	}

}
