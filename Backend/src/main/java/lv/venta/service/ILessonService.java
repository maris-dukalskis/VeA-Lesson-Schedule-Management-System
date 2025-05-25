package lv.venta.service;

import java.util.List;
import java.util.NoSuchElementException;

import lv.venta.model.Lesson;

public interface ILessonService {

	public abstract List<Lesson> selectAllLessons();

	public abstract Lesson selectLessonById(int id) throws IllegalArgumentException, NoSuchElementException;

	public abstract void deleteLessonById(int id);

	public abstract Lesson insertNewLesson(Lesson lesson) throws NullPointerException, IllegalStateException;

	public abstract Lesson updateLessonById(int id, Lesson lesson);

	public abstract List<Lesson> selectByStudyProgrammeNameAndYear(String name, int year);

	public abstract List<Lesson> selectByLecturerFullName(String name);

	public abstract List<Lesson> selectByClassroomBuildingAndNumber(String building, int number);

	public abstract List<Lesson> selectByClassroomId(int id);

	public abstract List<Lesson> selectByUserId(int id);

	public abstract List<Lesson> selectByCourseId(int id);

	public abstract List<Lesson> selectBySemesterId(int id);

}
