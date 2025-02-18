package lv.venta.repo;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.LessonDateTime;

public interface ILessonDateTimeRepo extends CrudRepository<LessonDateTime, Integer> {

	ArrayList<LessonDateTime> findByLessonLessonId(int id);

}
