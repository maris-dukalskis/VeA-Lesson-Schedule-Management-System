package lv.venta.service;

import java.util.List;
import java.util.NoSuchElementException;

import lv.venta.model.Course;

public interface ICourseService {

	public abstract List<Course> selectAllCourses();

	public abstract Course selectCourseById(int id) throws IllegalArgumentException, NoSuchElementException;

	public abstract void deleteCourseById(int id);

	public abstract Course insertNewCourse(Course course) throws NullPointerException, IllegalStateException;

	public abstract Course updateCourseById(int id, Course course);

	public abstract List<Course> selectByStudyProgrammeId(int id);

	public abstract List<Course> selectByUserId(int id);

}
