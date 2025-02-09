package lv.venta.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.LessonDateTime;

public interface ILessonDateTimeRepo extends CrudRepository<LessonDateTime, Integer> {

	List<LessonDateTime> findByLessonLessonId(int id);

}
