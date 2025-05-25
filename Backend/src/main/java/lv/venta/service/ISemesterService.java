package lv.venta.service;

import java.util.List;
import java.util.NoSuchElementException;

import lv.venta.model.Semester;

public interface ISemesterService {

	public abstract List<Semester> selectAllSemesters();

	public abstract Semester selectSemesterById(int id) throws IllegalArgumentException, NoSuchElementException;

	public abstract void deleteSemesterById(int id);

	public abstract Semester insertNewSemester(Semester semester) throws NullPointerException, IllegalStateException;

	public abstract Semester updateSemesterById(int id, Semester semester);

}
