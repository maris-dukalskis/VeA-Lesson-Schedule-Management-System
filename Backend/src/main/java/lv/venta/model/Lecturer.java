package lv.venta.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
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
public class Lecturer extends User {

	@Min(value = 0, message = "The value must be positive")
	private int hours; // how many hours they can teach per semester

	@ManyToMany(mappedBy = "lecturers")
	@JsonIgnore
	private List<Lesson> lessons;

	public Lecturer(String fullName, String email, Role role, int hours) {
		super(fullName, email, role);
		setHours(hours);
	}

}
