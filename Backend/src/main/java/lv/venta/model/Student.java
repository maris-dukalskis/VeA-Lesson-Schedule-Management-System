package lv.venta.model;

import jakarta.persistence.Entity;
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

	private StudyProgramme studyProgramme; //fk
	private int matriculeNumber;
	
	public Student(String fullName, String email, StudyProgramme studyProgramme, int matriculeNumber) {
		super(fullName, email);
		setStudyProgramme(studyProgramme);
		setMatriculeNumber(matriculeNumber);
	}
	
	

}
