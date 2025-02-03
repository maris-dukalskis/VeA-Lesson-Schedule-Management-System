package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.Lecturer;
import lv.venta.repo.ILecturerRepo;
import lv.venta.service.ILecturerService;

@Service
public class LecturerServiceImpl implements ILecturerService {

	@Autowired
	private ILecturerRepo lecturerRepo;

	@Override
	public ArrayList<Lecturer> selectAllLecturers() throws Exception {
		if (lecturerRepo.count() == 0)
			throw new Exception("Lecturer table is empty");
		return (ArrayList<Lecturer>) lecturerRepo.findAll();
	}

	@Override
	public Lecturer selectLecturerById(int id) throws Exception {
		if (id < 0)
			throw new Exception("ID cannot be below 0");
		if (!lecturerRepo.existsById(id)) {
			throw new Exception("Lecturer by that ID does not exist");
		}
		return lecturerRepo.findById(id).get();
	}

	@Override
	public void deleteLecturerById(int id) throws Exception {
		lecturerRepo.delete(selectLecturerById(id));
	}

	@Override
	public Lecturer insertNewLecturer(Lecturer lecturer) throws Exception {
		if (lecturer == null)
			throw new Exception("Lecturer object cannot be null");
		List<Lecturer> lecturers = selectAllLecturers();
		if (!lecturers.isEmpty()) {
			for (Lecturer dbLecturer : lecturers) {
				if (dbLecturer.getFullName().equals(lecturer.getFullName())
						&& dbLecturer.getEmail().equals(lecturer.getEmail())) {
					throw new Exception("Lecturer already exists");
				}
			}
		}
		return lecturerRepo.save(lecturer);
	}

	@Override
	public Lecturer updateLecturerById(int id, Lecturer lecturer) throws Exception {
		Lecturer oldLecturer = selectLecturerById(id);
		oldLecturer.setEmail(lecturer.getEmail());
		oldLecturer.setFullName(lecturer.getFullName());
		oldLecturer.setRole(lecturer.getRole());
		oldLecturer.setHours(lecturer.getHours());
		lecturerRepo.save(oldLecturer);
		return oldLecturer;
	}

}
