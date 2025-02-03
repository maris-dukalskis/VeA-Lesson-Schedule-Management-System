package lv.venta.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "StudyProgramme")
@Entity
/*
 * can be idividually per student based- a student has his own schedule so his
 * courses are not a part of a set programme
 */
public class StudyProgramme {

	@Id
	@Column(name = "StudyProgrammeId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(value = AccessLevel.NONE)
	private int studyProgrammeId;

	private int year; // 1st 2nd 3rd etc

	@OneToMany(mappedBy = "studyProgramme")
	@ToString.Exclude
	private List<Course> courses;

	@OneToMany(mappedBy = "studyProgramme")
	@ToString.Exclude
	private List<Student> students;

	private boolean isIndividual;

}
