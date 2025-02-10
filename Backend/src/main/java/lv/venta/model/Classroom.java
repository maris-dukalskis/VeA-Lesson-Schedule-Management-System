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
@Table(name = "Classroom")
@Entity
public class Classroom {

	@Id
	@Column(name = "ClassroomId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(value = AccessLevel.NONE)
	private int classroomId;

	@OneToMany(mappedBy = "classroom")
	@ToString.Exclude
	@JsonIgnore
	private List<Lesson> lessons;

	private String building;

	@Min(value = 0, message = "The value must be positive")
	private int number;

	private String equipmentDescription;

	public Classroom(String building, int number, String equipmentDescription) {
		setBuilding(building);
		setNumber(number);
		setEquipmentDescription(equipmentDescription);
	}

}
