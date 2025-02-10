package lv.venta.model;

import java.sql.Date;

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
@Table(name = "LessonDateTime")
@Entity
public class LessonDateTime {

	@Id
	@Column(name = "LessonDateTimeId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(value = AccessLevel.NONE)
	private int lessonDateTimeId;

	@ManyToOne
	@JoinColumn(name = "LessonId")
	private Lesson lesson;

	private boolean isCustom;

	private Date date;

	private String timeFrom;

	private String timeTo;

	public LessonDateTime( Lesson lesson,  boolean isCustom, Date date, String timeFrom, String timeTo) {
		setLesson(lesson);
		setCustom(isCustom);
		setDate(date);
		setTimeFrom(timeFrom);
		setTimeTo(timeTo);
	}

}
