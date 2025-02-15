package lv.venta.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
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
public class StudyProgramme {

	@Id
	@Column(name = "StudyProgrammeId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(value = AccessLevel.NONE)
	private int studyProgrammeId;

	private String name;

	@Min(value = 0, message = "The value must be positive")
	private int year; // 1st 2nd 3rd etc

	@OneToMany(mappedBy = "studyProgramme")
	@ToString.Exclude
	@JsonIgnore
	private List<Course> courses;

	public StudyProgramme(String name, int year) {
		setName(name);
		setYear(year);
	}

}
