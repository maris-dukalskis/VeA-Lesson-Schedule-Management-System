package lv.venta.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Entity
public class Student extends User {

	@ManyToOne
	@JoinColumn(name = "StudyProgrammeId")
	@ToString.Exclude
	private StudyProgramme studyProgramme;

	@ManyToMany(mappedBy = "students")
	@JsonIgnore
	private List<Lesson> lessons;

	@Min(value = 0, message = "The value must be positive")
	private int matriculeNumber;

	public Student(String fullName, String email, StudyProgramme studyProgramme, int matriculeNumber) {
		super(fullName, email, Role.STUDENT);
		setStudyProgramme(studyProgramme);
		setMatriculeNumber(matriculeNumber);
	}

}
