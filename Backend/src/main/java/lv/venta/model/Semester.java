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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "Semester")
@Entity
public class Semester {

	@Id
	@Column(name = "SemesterId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(value = AccessLevel.NONE)
	private int semesterId;

	@OneToMany(mappedBy = "semester")
	@ToString.Exclude
	@JsonIgnore
	private List<Lesson> lessons;

	private String name;

	private SemesterStatus semesterStatus;

	public Semester(String name, SemesterStatus semesterStatus) {
		setName(name);
		setSemesterStatus(semesterStatus);
	}

}
