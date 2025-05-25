package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import lv.venta.model.Lesson;
import lv.venta.model.Semester;
import lv.venta.repo.ILessonRepo;
import lv.venta.repo.ISemesterRepo;
import lv.venta.service.ILessonService;
import lv.venta.service.ISemesterService;

@Service
public class SemesterServiceImpl implements ISemesterService {

	private final ISemesterRepo semesterRepo;

	private final ILessonService lessonService;

	private final ILessonRepo lessonRepo;

	public SemesterServiceImpl(ISemesterRepo semesterRepo, ILessonService lessonService, ILessonRepo lessonRepo) {
		this.semesterRepo = semesterRepo;
		this.lessonService = lessonService;
		this.lessonRepo = lessonRepo;
	}

	@Override
	public List<Semester> selectAllSemesters() {
		if (semesterRepo.count() == 0)
			return new ArrayList<>();
		return (List<Semester>) semesterRepo.findAll();
	}

	@Override
	public Semester selectSemesterById(int id) throws IllegalArgumentException, NoSuchElementException {
		if (id < 0)
			throw new IllegalArgumentException("ID cannot be below 0");
		if (!semesterRepo.existsById(id)) {
			throw new NoSuchElementException("Semester by that ID does not exist");
		}
		return semesterRepo.findById(id).get();
	}

	@Override
	public void deleteSemesterById(int id) {
		List<Lesson> lessons = lessonService.selectBySemesterId(id);
		if (!lessons.isEmpty()) {
			for (Lesson lesson : lessons) {
				lesson.setSemester(null);
			}
			lessonRepo.saveAll(lessons);
		}
		semesterRepo.delete(selectSemesterById(id));
	}

	@Override
	public Semester insertNewSemester(Semester semester) throws NullPointerException, IllegalStateException {
		if (semester == null)
			throw new NullPointerException("Semester object cannot be null");

		List<Semester> semesters = selectAllSemesters();

		if (!semesters.isEmpty()) {
			for (Semester dbSemester : semesters) {
				if (dbSemester.getName().equals(semester.getName())) {
					throw new IllegalStateException("Semester already exists");
				}
			}
		}
		return semesterRepo.save(semester);
	}

	@Override
	public Semester updateSemesterById(int id, Semester semester) {
		Semester oldSemester = selectSemesterById(id);
		oldSemester.setName(semester.getName());
		oldSemester.setSemesterStatus(semester.getSemesterStatus());
		semesterRepo.save(oldSemester);
		return oldSemester;
	}

}
