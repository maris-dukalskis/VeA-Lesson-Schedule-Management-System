package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;

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
	public ArrayList<Lesson> selectAllLessons() throws Exception {
		if (lessonRepo.count() == 0)
			return new ArrayList<Lesson>();
		return (ArrayList<Lesson>) lessonRepo.findAll();
	}

	@Override
	public Lesson selectLessonById(int id) throws Exception {
		if (id < 0)
			throw new Exception("ID cannot be below 0");
		if (!lessonRepo.existsById(id)) {
			throw new Exception("Lesson by that ID does not exist");
		}
		return lessonRepo.findById(id).get();
	}

	@Override
	public void deleteLessonById(int id) throws Exception {
		ArrayList<LessonDateTime> lessonTimes = lessonDateTimeService.selectAllByLessonLessonId(id);
		if (!lessonTimes.isEmpty()) {
			for (LessonDateTime lessonDateTime : lessonTimes) {
				lessonDateTimeService.deleteLessonDateTimeById(lessonDateTime.getLessonDateTimeId());
			}
		}
		lessonRepo.delete(selectLessonById(id));
	}

	@Override
	public Lesson insertNewLesson(Lesson lesson) throws Exception {
		if (lesson == null)
			throw new Exception("Lesson object cannot be null");
		List<Lesson> lessons = new ArrayList<>();
		try {
			lessons = selectAllLessons();
		} catch (Exception e) {
		}
		if (!lessons.isEmpty()) {
			for (Lesson dbLesson : lessons) {
				if (dbLesson.getCourse().getCourseId() == lesson.getCourse().getCourseId()
						&& dbLesson.getLessonGroup() == lesson.getLessonGroup()) {
					throw new Exception("Lesson already exists");
				}
			}
		}
		return lessonRepo.save(lesson);
	}

	@Override
	public Lesson updateLessonById(int id, Lesson lesson) throws Exception {
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
	public ArrayList<Lesson> selectByStudyProgrammeNameAndYear(String name, int year) throws Exception {
		if (lessonRepo.count() == 0)
			return new ArrayList<Lesson>();
		return (ArrayList<Lesson>) lessonRepo
				.findByCourseCourseStudyProgrammeAliasesStudyProgrammeNameAndCourseCourseStudyProgrammeAliasesStudyProgrammeYear(
						name, year);
	}

	@Override
	public ArrayList<Lesson> selectByLecturerFullName(String name) throws Exception {
		if (lessonRepo.count() == 0)
			return new ArrayList<Lesson>();
		return (ArrayList<Lesson>) lessonRepo.findByLecturerFullName(name);
	}

	@Override
	public ArrayList<Lesson> selectByClassroomBuildingAndNumber(String building, int number) throws Exception {
		if (lessonRepo.count() == 0)
			return new ArrayList<Lesson>();
		return (ArrayList<Lesson>) lessonRepo.findByClassroomBuildingAndClassroomNumber(building, number);
	}

	@Override
	public ArrayList<Lesson> selectByClassroomId(int id) throws Exception {
		if (lessonRepo.count() == 0)
			return new ArrayList<Lesson>();
		return (ArrayList<Lesson>) lessonRepo.findByClassroomClassroomId(id);
	}

	@Override
	public ArrayList<Lesson> selectByUserId(int id) throws Exception {
		if (lessonRepo.count() == 0)
			return new ArrayList<Lesson>();
		return (ArrayList<Lesson>) lessonRepo.findByLecturerUserId(id);
	}

	@Override
	public ArrayList<Lesson> selectByCourseId(int id) throws Exception {
		if (lessonRepo.count() == 0)
			return new ArrayList<Lesson>();
		return (ArrayList<Lesson>) lessonRepo.findByCourseCourseId(id);
	}

	@Override
	public ArrayList<Lesson> selectBySemesterId(int id) throws Exception {
		if (lessonRepo.count() == 0)
			return new ArrayList<Lesson>();
		return (ArrayList<Lesson>) lessonRepo.findBySemesterSemesterId(id);
	}

}
