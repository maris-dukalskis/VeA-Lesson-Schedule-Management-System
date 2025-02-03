package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.Classroom;

public interface IClassroomService {

	public abstract ArrayList<Classroom> selectAllClassrooms() throws Exception;

	public abstract Classroom selectClassroomById(int id) throws Exception;

	public abstract void deleteClassroomById(int id) throws Exception;

	public abstract Classroom insertNewClassroom(Classroom classroom) throws Exception;

	public abstract Classroom updateClassroomById(int id, Classroom classroom) throws Exception;

}
