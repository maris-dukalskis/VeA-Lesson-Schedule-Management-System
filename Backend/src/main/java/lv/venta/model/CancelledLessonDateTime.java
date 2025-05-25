package lv.venta.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "CancelledLessonDateTime")
@Entity
public class CancelledLessonDateTime {

	@Id
	@Column(name = "CancelledLessonDateTimeId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(value = AccessLevel.NONE)
	private int cancelledLessonDateTimeId;

	@OneToOne
	@JoinColumn(name = "LessonDateTimeId")
	private LessonDateTime lessonDateTime;

	private boolean isRescheduled;

	private String reason;

	private Date date;

	public CancelledLessonDateTime(LessonDateTime lessonDateTime, boolean isRescheduled, String reason, Date date,
			String rescheduledTimeFrom, String rescheduledTimeTo) {
		setLessonDateTime(lessonDateTime);
		setRescheduled(isRescheduled);
		setReason(reason);
		setDate(date);
	}

}
