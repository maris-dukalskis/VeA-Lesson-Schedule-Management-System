package lv.venta.service;

import java.util.List;
import java.util.NoSuchElementException;

import lv.venta.model.LessonDateTime;

public interface ILessonDateTimeService {

	public abstract List<LessonDateTime> selectAllLessonDateTimes();

	public abstract LessonDateTime selectLessonDateTimeById(int id)
			throws IllegalArgumentException, NoSuchElementException;

	public abstract void deleteLessonDateTimeById(int id);

	public abstract LessonDateTime insertNewLessonDateTime(LessonDateTime lessonDateTime)
			throws NullPointerException, IllegalStateException;

	public abstract LessonDateTime updateLessonDateTimeById(int id, LessonDateTime lessonDateTime);

	public abstract List<LessonDateTime> selectAllByLessonLessonId(int id);

}
