package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.Course;

public interface ICourseService {

	public abstract ArrayList<Course> selectAllCourses() throws Exception;

	public abstract Course selectCourseById(int id) throws Exception;

	public abstract void deleteCourseById(int id) throws Exception;

	public abstract Course insertNewCourse(Course course) throws Exception;

	public abstract Course updateCourseById(int id, Course course) throws Exception;

}
