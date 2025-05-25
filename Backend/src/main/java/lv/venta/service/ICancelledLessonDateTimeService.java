package lv.venta.service;

import java.util.List;
import java.util.NoSuchElementException;

import lv.venta.model.CancelledLessonDateTime;

public interface ICancelledLessonDateTimeService {

	public abstract List<CancelledLessonDateTime> selectAllCancelledLessonDateTimes();

	public abstract CancelledLessonDateTime selectCancelledLessonDateTimeById(int id)
			throws IllegalArgumentException, NoSuchElementException;

	public abstract void deleteCancelledLessonDateTimeById(int id);

	public abstract CancelledLessonDateTime insertNewCancelledLessonDateTime(
			CancelledLessonDateTime cancelledLessonDateTime) throws NullPointerException, IllegalStateException;

	public abstract CancelledLessonDateTime updateCancelledLessonDateTimeById(int id,
			CancelledLessonDateTime cancelledLessonDateTime);

	public abstract List<CancelledLessonDateTime> selectByLessonDateTimeId(int id);

}
