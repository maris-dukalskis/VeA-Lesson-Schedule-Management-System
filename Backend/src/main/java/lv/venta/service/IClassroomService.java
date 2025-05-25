package lv.venta.service;

import java.util.List;
import java.util.NoSuchElementException;

import lv.venta.model.Classroom;

public interface IClassroomService {

	public abstract List<Classroom> selectAllClassrooms();

	public abstract Classroom selectClassroomById(int id) throws IllegalArgumentException, NoSuchElementException;

	public abstract void deleteClassroomById(int id);

	public abstract Classroom insertNewClassroom(Classroom classroom)
			throws NullPointerException, IllegalStateException;

	public abstract Classroom updateClassroomById(int id, Classroom classroom);

}
