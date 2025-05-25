package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import lv.venta.model.Lesson;
import lv.venta.model.LessonDateTime;
import lv.venta.repo.ILessonRepo;
import lv.venta.service.ILessonDateTimeService;
import lv.venta.service.ILessonService;

@Service
public class LessonServiceImpl implements ILessonService {

	private final ILessonRepo lessonRepo;

	private final ILessonDateTimeService lessonDateTimeService;

	public LessonServiceImpl(ILessonRepo lessonRepo, ILessonDateTimeService lessonDateTimeService) {
		this.lessonRepo = lessonRepo;
		this.lessonDateTimeService = lessonDateTimeService;
	}

	@Override
	public List<Lesson> selectAllLessons() {
		if (lessonRepo.count() == 0)
			return new ArrayList<>();
		return (List<Lesson>) lessonRepo.findAll();
	}

	@Override
	public Lesson selectLessonById(int id) throws IllegalArgumentException, NoSuchElementException {
		if (id < 0)
			throw new IllegalArgumentException("ID cannot be below 0");
		if (!lessonRepo.existsById(id)) {
			throw new NoSuchElementException("Lesson by that ID does not exist");
		}
		return lessonRepo.findById(id).get();
	}

	@Override
	public void deleteLessonById(int id) {
		List<LessonDateTime> lessonTimes = lessonDateTimeService.selectAllByLessonLessonId(id);
		if (!lessonTimes.isEmpty()) {
			for (LessonDateTime lessonDateTime : lessonTimes) {
				lessonDateTimeService.deleteLessonDateTimeById(lessonDateTime.getLessonDateTimeId());
			}
		}
		lessonRepo.delete(selectLessonById(id));
	}

	@Override
	public Lesson insertNewLesson(Lesson lesson) throws NullPointerException, IllegalStateException {
		if (lesson == null)
			throw new NullPointerException("Lesson object cannot be null");

		List<Lesson> lessons = selectAllLessons();

		if (!lessons.isEmpty()) {
			for (Lesson dbLesson : lessons) {
				if (dbLesson.getCourse().getCourseId() == lesson.getCourse().getCourseId()
						&& dbLesson.getLessonGroup() == lesson.getLessonGroup()) {
					throw new IllegalStateException("Lesson already exists");
				}
			}
		}
		return lessonRepo.save(lesson);
	}

	@Override
	public Lesson updateLessonById(int id, Lesson lesson) {
		Lesson oldLesson = selectLessonById(id);
		oldLesson.setCourse(lesson.getCourse());
		oldLesson.setLessonGroup(lesson.getLessonGroup());
		oldLesson.setOnline(lesson.isOnline());
		oldLesson.setOnlineInformation(lesson.getOnlineInformation());
		oldLesson.setLecturer(lesson.getLecturer());
		oldLesson.setClassroom(lesson.getClassroom());
		oldLesson.setSemester(lesson.getSemester());
		lessonRepo.save(oldLesson);
		return oldLesson;
	}

	@Override
	public List<Lesson> selectByStudyProgrammeNameAndYear(String name, int year) {
		if (lessonRepo.count() == 0)
			return new ArrayList<>();
		return (List<Lesson>) lessonRepo
				.findByCourseCourseStudyProgrammeAliasesStudyProgrammeNameAndCourseCourseStudyProgrammeAliasesStudyProgrammeYear(
						name, year);
	}

	@Override
	public List<Lesson> selectByLecturerFullName(String name) {
		if (lessonRepo.count() == 0)
			return new ArrayList<>();
		return (List<Lesson>) lessonRepo.findByLecturerFullName(name);
	}

	@Override
	public List<Lesson> selectByClassroomBuildingAndNumber(String building, int number) {
		if (lessonRepo.count() == 0)
			return new ArrayList<>();
		return (List<Lesson>) lessonRepo.findByClassroomBuildingAndClassroomNumber(building, number);
	}

	@Override
	public List<Lesson> selectByClassroomId(int id) {
		if (lessonRepo.count() == 0)
			return new ArrayList<>();
		return (List<Lesson>) lessonRepo.findByClassroomClassroomId(id);
	}

	@Override
	public List<Lesson> selectByUserId(int id) {
		if (lessonRepo.count() == 0)
			return new ArrayList<>();
		return (List<Lesson>) lessonRepo.findByLecturerUserId(id);
	}

	@Override
	public List<Lesson> selectByCourseId(int id) {
		if (lessonRepo.count() == 0)
			return new ArrayList<>();
		return (List<Lesson>) lessonRepo.findByCourseCourseId(id);
	}

	@Override
	public List<Lesson> selectBySemesterId(int id) {
		if (lessonRepo.count() == 0)
			return new ArrayList<>();
		return (List<Lesson>) lessonRepo.findBySemesterSemesterId(id);
	}

}
