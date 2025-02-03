package lv.venta.repo;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Lesson;

public interface ILessonRepo extends CrudRepository<Lesson, Integer> {

}
