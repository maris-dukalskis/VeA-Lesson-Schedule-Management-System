package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.Lesson;
import lv.venta.repo.ILessonRepo;
import lv.venta.service.ILessonService;

@Service
public class LessonServiceImpl implements ILessonService {

	@Autowired
	ILessonRepo lessonRepo;

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
				if (dbLesson.getCourse().equals(lesson.getCourse())) { // TODO this might need to be changed(if
																		// lesson:course is 1:1)
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
		oldLesson.setOnline(lesson.isOnline());
		oldLesson.setOnlineInformation(lesson.getOnlineInformation());
		oldLesson.setDate(lesson.getDate());
		oldLesson.setTime(lesson.getTime());
		lessonRepo.save(oldLesson);
		return oldLesson;
	}

}
