package lv.venta.repo;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Lesson;

public interface ILessonRepo extends CrudRepository<Lesson, Integer> {

	ArrayList<Lesson> findByCourseStudyProgrammesNameAndCourseStudyProgrammesYear(String name, int year);

}
