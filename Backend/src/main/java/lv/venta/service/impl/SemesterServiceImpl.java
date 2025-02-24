package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.Lesson;
import lv.venta.model.Semester;
import lv.venta.repo.ILessonRepo;
import lv.venta.repo.ISemesterRepo;
import lv.venta.service.ILessonService;
import lv.venta.service.ISemesterService;

@Service
public class SemesterServiceImpl implements ISemesterService {

	@Autowired
	private ISemesterRepo semesterRepo;

	@Autowired
	private ILessonService lessonService;

	@Autowired
	private ILessonRepo lessonRepo;

	@Override
	public ArrayList<Semester> selectAllSemesters() throws Exception {
		if (semesterRepo.count() == 0)
			return new ArrayList<Semester>();
		return (ArrayList<Semester>) semesterRepo.findAll();
	}

	@Override
	public Semester selectSemesterById(int id) throws Exception {
		if (id < 0)
			throw new Exception("ID cannot be below 0");
		if (!semesterRepo.existsById(id)) {
			throw new Exception("Semester by that ID does not exist");
		}
		return semesterRepo.findById(id).get();
	}

	@Override
	public void deleteSemesterById(int id) throws Exception {
		ArrayList<Lesson> lessons = lessonService.selectBySemesterId(id);
		if (!lessons.isEmpty()) {
			for (Lesson lesson : lessons) {
				lesson.setSemester(null);
			}
			lessonRepo.saveAll(lessons);
		}
		semesterRepo.delete(selectSemesterById(id));
	}

	@Override
	public Semester insertNewSemester(Semester semester) throws Exception {
		if (semester == null)
			throw new Exception("Semester object cannot be null");
		List<Semester> semesters = new ArrayList<>();
		try {
			semesters = selectAllSemesters();
		} catch (Exception e) {
		}
		if (!semesters.isEmpty()) {
			for (Semester dbSemester : semesters) {
				if (dbSemester.getName().equals(dbSemester.getName())) {
					throw new Exception("Semester already exists");
				}
			}
		}
		return semesterRepo.save(semester);
	}

	@Override
	public Semester updateSemesterById(int id, Semester semester) throws Exception {
		Semester oldSemester = selectSemesterById(id);
		oldSemester.setName(semester.getName());
		oldSemester.setSemesterStatus(semester.getSemesterStatus());
		semesterRepo.save(oldSemester);
		return oldSemester;
	}

}
