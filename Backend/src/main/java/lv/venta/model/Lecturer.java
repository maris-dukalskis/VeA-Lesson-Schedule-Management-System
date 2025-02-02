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
@Table(name = "Lecturer")
@Entity
public class Lecturer extends User {

	private Role role;
	private int hours; // how many hours they can teach per semester

	public Lecturer(String fullName, String email, Role role, int hours) {
		super(fullName, email);
		this.role = role;
		this.hours = hours;
	}

}
