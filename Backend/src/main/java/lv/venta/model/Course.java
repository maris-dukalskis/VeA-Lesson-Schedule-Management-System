package lv.venta.model;

import java.util.List;

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
@Table(name = "Course")
@Entity
public class Course {

	@Id
	@Column(name = "CourseId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(value = AccessLevel.NONE)
	private int courseId;

	private String name;

	private String description;
	
//	private int creditpoints; TODO implement this

	@OneToMany(mappedBy = "course")
	@ToString.Exclude
	@JsonIgnore
	private List<Lesson> lessons;

	@ManyToOne
	@JoinColumn(name = "StudyProgrammeId")
	@ToString.Exclude
	private StudyProgramme studyProgramme;

	public Course(String name, String description, StudyProgramme studyProgramme) {
		setName(name);
		setDescription(description);
		setStudyProgramme(studyProgramme);
	}

}
