package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import lv.venta.model.Lecturer;
import lv.venta.repo.ILecturerRepo;
import lv.venta.service.ILecturerService;

@Service
public class LecturerServiceImpl implements ILecturerService {

	private final ILecturerRepo lecturerRepo;

	public LecturerServiceImpl(ILecturerRepo lecturerRepo) {
		this.lecturerRepo = lecturerRepo;
	}

	@Override
	public List<Lecturer> selectAllLecturers() {
		if (lecturerRepo.count() == 0)
			return new ArrayList<>();
		return (List<Lecturer>) lecturerRepo.findAll();
	}

	@Override
	public Lecturer selectLecturerById(int id) throws IllegalArgumentException, NoSuchElementException {
		if (id < 0)
			throw new IllegalArgumentException("ID cannot be below 0");
		if (!lecturerRepo.existsById(id)) {
			throw new NoSuchElementException("Lecturer by that ID does not exist");
		}
		return lecturerRepo.findById(id).get();
	}

	@Override
	public void deleteLecturerById(int id) {
		lecturerRepo.delete(selectLecturerById(id));
	}

	@Override
	public Lecturer insertNewLecturer(Lecturer lecturer) throws NullPointerException, IllegalStateException {
		if (lecturer == null)
			throw new NullPointerException("Lecturer object cannot be null");

		List<Lecturer> lecturers = selectAllLecturers();

		if (!lecturers.isEmpty()) {
			for (Lecturer dbLecturer : lecturers) {
				if (dbLecturer.getFullName().equals(lecturer.getFullName())
						&& dbLecturer.getEmail().equals(lecturer.getEmail())) {
					throw new IllegalStateException("Lecturer already exists");
				}
			}
		}
		return lecturerRepo.save(lecturer);
	}

	@Override
	public Lecturer updateLecturerById(int id, Lecturer lecturer) {
		Lecturer oldLecturer = selectLecturerById(id);
		oldLecturer.setEmail(lecturer.getEmail());
		oldLecturer.setFullName(lecturer.getFullName());
		oldLecturer.setRole(lecturer.getRole());
		oldLecturer.setHours(lecturer.getHours());
		oldLecturer.setNotes(lecturer.getNotes());
		oldLecturer.setSeniority(lecturer.getSeniority());
		lecturerRepo.save(oldLecturer);
		return oldLecturer;
	}

}
