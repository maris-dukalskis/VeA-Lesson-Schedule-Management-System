package lv.venta.service;

import java.util.List;
import java.util.NoSuchElementException;

import lv.venta.model.Lecturer;

public interface ILecturerService {

	public abstract List<Lecturer> selectAllLecturers();

	public abstract Lecturer selectLecturerById(int id) throws IllegalArgumentException, NoSuchElementException;

	public abstract void deleteLecturerById(int id);

	public abstract Lecturer insertNewLecturer(Lecturer lecturer) throws NullPointerException, IllegalStateException;

	public abstract Lecturer updateLecturerById(int id, Lecturer lecturer);

}
