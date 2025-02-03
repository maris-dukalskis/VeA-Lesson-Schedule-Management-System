package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.Lecturer;

public interface ILecturerService {

	public abstract ArrayList<Lecturer> selectAllLecturers() throws Exception;

	public abstract Lecturer selectLecturerById(int id) throws Exception;

	public abstract void deleteLecturerById(int id) throws Exception;

	public abstract Lecturer insertNewLecturer(Lecturer lecturer) throws Exception;

	public abstract Lecturer updateLecturerById(int id, Lecturer lecturer) throws Exception;

}
