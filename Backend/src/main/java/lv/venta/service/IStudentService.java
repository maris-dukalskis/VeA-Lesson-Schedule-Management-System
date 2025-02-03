package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.Student;

public interface IStudentService {

	public abstract ArrayList<Student> selectAllStudents() throws Exception;

	public abstract Student selectStudentById(int id) throws Exception;

	public abstract void deleteStudentById(int id) throws Exception;

	public abstract Student insertNewStudent(Student student) throws Exception;

	public abstract Student updateStudentById(int id, Student student) throws Exception;

}
