package lv.venta.repo;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.CancelledLessonDateTime;

public interface ICancelledLessonDateTimeRepo extends CrudRepository<CancelledLessonDateTime, Integer> {

	ArrayList<CancelledLessonDateTime> findByLessonDateTimeLessonDateTimeId(int id);

}
