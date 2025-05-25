package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.CancelledLessonDateTime;

public interface ICancelledLessonDateTimeService {

	public abstract ArrayList<CancelledLessonDateTime> selectAllCancelledLessonDateTimes() throws Exception;

	public abstract CancelledLessonDateTime selectCancelledLessonDateTimeById(int id) throws Exception;

	public abstract void deleteCancelledLessonDateTimeById(int id) throws Exception;

	public abstract CancelledLessonDateTime insertNewCancelledLessonDateTime(
			CancelledLessonDateTime cancelledLessonDateTime) throws Exception;

	public abstract CancelledLessonDateTime updateCancelledLessonDateTimeById(int id,
			CancelledLessonDateTime cancelledLessonDateTime) throws Exception;

	public abstract ArrayList<CancelledLessonDateTime> selectByLessonDateTimeId(int id) throws Exception;

}
