package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import lv.venta.model.Classroom;
import lv.venta.model.Lesson;
import lv.venta.repo.IClassroomRepo;
import lv.venta.repo.ILessonRepo;
import lv.venta.service.IClassroomService;
import lv.venta.service.ILessonService;

@Service
public class ClassroomServiceImpl implements IClassroomService {

	private final IClassroomRepo classroomRepo;

	private final ILessonService lessonService;

	private final ILessonRepo lessonRepo;

	public ClassroomServiceImpl(IClassroomRepo classroomRepo, ILessonService lessonService, ILessonRepo lessonRepo) {
		this.classroomRepo = classroomRepo;
		this.lessonService = lessonService;
		this.lessonRepo = lessonRepo;
	}

	@Override
	public List<Classroom> selectAllClassrooms() {
		if (classroomRepo.count() == 0)
			return new ArrayList<>();
		return (List<Classroom>) classroomRepo.findAll();
	}

	@Override
	public Classroom selectClassroomById(int id) throws IllegalArgumentException, NoSuchElementException {
		if (id < 0)
			throw new IllegalArgumentException("ID cannot be below 0");
		if (!classroomRepo.existsById(id)) {
			throw new NoSuchElementException("Classroom by that ID does not exist");
		}
		return classroomRepo.findById(id).get();
	}

	@Override
	public void deleteClassroomById(int id) {
		List<Lesson> lessons = lessonService.selectByClassroomId(id);
		if (!lessons.isEmpty()) {
			for (Lesson lesson : lessons) {
				lesson.setClassroom(null);
			}
			lessonRepo.saveAll(lessons);
		}
		classroomRepo.delete(selectClassroomById(id));
	}

	@Override
	public Classroom insertNewClassroom(Classroom classroom) throws NullPointerException, IllegalStateException {
		if (classroom == null)
			throw new NullPointerException("Classroom object cannot be null");

		List<Classroom> classrooms = selectAllClassrooms();

		if (!classrooms.isEmpty()) {
			for (Classroom dbClassroom : classrooms) {
				if (dbClassroom.getBuilding().equals(classroom.getBuilding())
						&& dbClassroom.getNumber() == classroom.getNumber()) {
					throw new IllegalStateException("Classroom already exists");
				}
			}
		}
		return classroomRepo.save(classroom);
	}

	@Override
	public Classroom updateClassroomById(int id, Classroom classroom) {
		Classroom oldClassroom = selectClassroomById(id);
		oldClassroom.setBuilding(classroom.getBuilding());
		oldClassroom.setEquipmentDescription(classroom.getEquipmentDescription());
		oldClassroom.setNumber(classroom.getNumber());
		oldClassroom.setSeats(classroom.getSeats());
		classroomRepo.save(oldClassroom);
		return oldClassroom;
	}

}
