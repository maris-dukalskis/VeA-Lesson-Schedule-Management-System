package lv.venta.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "Course")
@Entity
public class Course {

	@Id
	@Column(name = "CourseId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(value = AccessLevel.NONE)
	private int courseId;

	@ManyToMany
	@JoinTable(name = "Course_User", joinColumns = @JoinColumn(name = "CourseId"), inverseJoinColumns = @JoinColumn(name = "UserId"))
	@ToString.Exclude
	private List<User> users;

	@OneToMany(mappedBy = "course")
	@ToString.Exclude
	@JsonIgnore
	private List<Lesson> lessons;

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	@ToString.Exclude
	@JsonIgnore
	private List<CourseStudyProgrammeAlias> courseStudyProgrammeAliases;

	private String name;

	private String shortName;

	private String description;

	@Min(value = 0, message = "The value must be positive")
	private int creditPoints;

	public Course(String name, String shortName, String description, int creditPoints) {
		setName(name);
		setShortName(shortName);
		setDescription(description);
		setCreditPoints(creditPoints);
	}
}
