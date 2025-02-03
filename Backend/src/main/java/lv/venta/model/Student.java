package lv.venta.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "Student")
@Entity
public class Student extends User {

	@ManyToOne
	@JoinColumn(name = "StudyProgrammeId")
	@ToString.Exclude
	private StudyProgramme studyProgramme;

	@ManyToMany(mappedBy = "students")
	private List<Lesson> lessons;

	private int matriculeNumber;

	public Student(String fullName, String email, StudyProgramme studyProgramme, int matriculeNumber) {
		super(fullName, email);
		setStudyProgramme(studyProgramme);
		setMatriculeNumber(matriculeNumber);
	}

}
