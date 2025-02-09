package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.LessonDateTime;

public interface ILessonDateTimeService {

	public abstract ArrayList<LessonDateTime> selectAllLessonDateTimes() throws Exception;

	public abstract LessonDateTime selectLessonDateTimeById(int id) throws Exception;

	public abstract void deleteLessonDateTimeById(int id) throws Exception;

	public abstract LessonDateTime insertNewLessonDateTime(LessonDateTime lessonDateTime) throws Exception;

	public abstract LessonDateTime updateLessonDateTimeById(int id, LessonDateTime lessonDateTime) throws Exception;

	public abstract ArrayList<LessonDateTime> selectAllByLessonLessonId(int id) throws Exception;

}
