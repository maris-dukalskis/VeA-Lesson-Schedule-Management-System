package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.Semester;

public interface ISemesterService {

	public abstract ArrayList<Semester> selectAllSemesters() throws Exception;

	public abstract Semester selectSemesterById(int id) throws Exception;

	public abstract void deleteSemesterById(int id) throws Exception;

	public abstract Semester insertNewSemester(Semester semester) throws Exception;

	public abstract Semester updateSemesterById(int id, Semester semester) throws Exception;

}
