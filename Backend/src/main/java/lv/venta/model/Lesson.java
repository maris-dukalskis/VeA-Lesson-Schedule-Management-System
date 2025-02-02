package lv.venta.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	
	private Course course; //fk
	
	private Lecturer lecturer; // can be more than 1(class is split into 2 groups) //fk
	
	private Classroom classRoom; //fk
	
	private List<Student> studentList; //list of students attending //fk
	
	private boolean isOnline;
	
	private String onlineInformation;
	
//	private int studentId; is this needed?
	private LocalDate date; // TODO make this into a list
	
	private LocalTime time; // TODO make this into a list

}
