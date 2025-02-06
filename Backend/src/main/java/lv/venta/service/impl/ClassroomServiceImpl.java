package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.Classroom;
import lv.venta.repo.IClassroomRepo;
import lv.venta.service.IClassroomService;

@Service
public class ClassroomServiceImpl implements IClassroomService {

	@Autowired
	private IClassroomRepo classroomRepo;

	@Override
	public ArrayList<Classroom> selectAllClassrooms() throws Exception {
		if (classroomRepo.count() == 0)
			throw new Exception("Classroom table is empty");
		return (ArrayList<Classroom>) classroomRepo.findAll();
	}

	@Override
	public Classroom selectClassroomById(int id) throws Exception {
		if (id < 0)
			throw new Exception("ID cannot be below 0");
		if (!classroomRepo.existsById(id)) {
			throw new Exception("Classroom by that ID does not exist");
		}
		return classroomRepo.findById(id).get();
	}

	@Override
	public void deleteClassroomById(int id) throws Exception {
		classroomRepo.delete(selectClassroomById(id));
	}

	@Override
	public Classroom insertNewClassroom(Classroom classroom) throws Exception {
		if (classroom == null)
			throw new Exception("Classroom object cannot be null");
		List<Classroom> classrooms = new ArrayList<>();
		try {
			classrooms = selectAllClassrooms();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!classrooms.isEmpty()) {
			for (Classroom dbClassroom : classrooms) {
				if (dbClassroom.getBuilding().equals(classroom.getBuilding())
						&& dbClassroom.getNumber() == classroom.getNumber()) {
					throw new Exception("Classroom already exists");
				}
			}
		}
		return classroomRepo.save(classroom);
	}

	@Override
	public Classroom updateClassroomById(int id, Classroom classroom) throws Exception {
		Classroom oldClassroom = selectClassroomById(id);
		oldClassroom.setBuilding(classroom.getBuilding());
		oldClassroom.setEquipmentDescription(classroom.getEquipmentDescription());
		oldClassroom.setNumber(classroom.getNumber());
		classroomRepo.save(oldClassroom);
		return oldClassroom;
	}

}
