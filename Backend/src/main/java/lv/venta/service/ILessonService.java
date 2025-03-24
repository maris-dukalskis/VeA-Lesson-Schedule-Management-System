package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.Lesson;

public interface ILessonService {

	public abstract ArrayList<Lesson> selectAllLessons() throws Exception;

	public abstract Lesson selectLessonById(int id) throws Exception;

	public abstract void deleteLessonById(int id) throws Exception;

	public abstract Lesson insertNewLesson(Lesson lesson) throws Exception;

	public abstract Lesson updateLessonById(int id, Lesson lesson) throws Exception;

	public abstract ArrayList<Lesson> selectByStudyProgrammeNameAndYear(String name, int year) throws Exception;

	public abstract ArrayList<Lesson> selectByLecturerFullName(String name) throws Exception;

	public abstract ArrayList<Lesson> selectByClassroomBuildingAndNumber(String building, int number) throws Exception;

	public abstract ArrayList<Lesson> selectByClassroomId(int id) throws Exception;

	public abstract ArrayList<Lesson> selectByUserId(int id) throws Exception;

	public abstract ArrayList<Lesson> selectByCourseId(int id) throws Exception;

	public abstract ArrayList<Lesson> selectBySemesterId(int id) throws Exception;

}
