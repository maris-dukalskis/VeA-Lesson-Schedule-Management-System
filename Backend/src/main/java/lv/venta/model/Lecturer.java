package lv.venta.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class Lecturer extends User {

	@Min(value = 0, message = "The value must be positive")
	private int hours; // how many hours they can teach per semester

	@OneToMany(mappedBy = "lecturer")
	@ToString.Exclude
	@JsonIgnore
	private List<Lesson> lessons;

	private String notes;

	private String seniority;

	public Lecturer(String fullName, String email, Role role, int hours, String notes, String seniority) {
		super(fullName, email, role);
		setHours(hours);
		setNotes(notes);
		setSeniority(seniority);
	}

}
