package lv.venta.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "CourseStudyProgrammeAlias")
@Entity
public class CourseStudyProgrammeAlias {

	@Id
	@Column(name = "CourseStudyProgrammeAliasId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(value = AccessLevel.NONE)
	private int courseStudyProgrammeAliasId;

	@ManyToOne
	@JoinColumn(name = "CourseId")
	private Course course;

	@ManyToOne
	@JoinColumn(name = "StudyProgrammeId")
	private StudyProgramme studyProgramme;

	private String alias;

	public CourseStudyProgrammeAlias(Course course, StudyProgramme studyProgramme, String alias) {
		setCourse(course);
		setStudyProgramme(studyProgramme);
		setAlias(alias);
	}

}
