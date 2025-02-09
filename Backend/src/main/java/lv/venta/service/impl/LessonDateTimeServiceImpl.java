package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.LessonDateTime;
import lv.venta.repo.ILessonDateTimeRepo;
import lv.venta.service.ILessonDateTimeService;

@Service
public class LessonDateTimeServiceImpl implements ILessonDateTimeService {

	@Autowired
	private ILessonDateTimeRepo lessonDateTimeRepo;

	@Override
	public ArrayList<LessonDateTime> selectAllLessonDateTimes() throws Exception {
		if (lessonDateTimeRepo.count() == 0)
			return new ArrayList<LessonDateTime>();
		return (ArrayList<LessonDateTime>) lessonDateTimeRepo.findAll();
	}

	@Override
	public LessonDateTime selectLessonDateTimeById(int id) throws Exception {
		if (id < 0)
			throw new Exception("ID cannot be below 0");
		if (!lessonDateTimeRepo.existsById(id)) {
			throw new Exception("LessonDateTime by that ID does not exist");
		}
		return lessonDateTimeRepo.findById(id).get();
	}

	@Override
	public void deleteLessonDateTimeById(int id) throws Exception {
		lessonDateTimeRepo.delete(selectLessonDateTimeById(id));
	}

	@Override
	public LessonDateTime insertNewLessonDateTime(LessonDateTime lessonDateTime) throws Exception {
		if (lessonDateTime == null)
			throw new Exception("LessonDateTime object cannot be null");
		List<LessonDateTime> lessonDateTimes = new ArrayList<>();
		try {
			lessonDateTimes = selectAllLessonDateTimes();
		} catch (Exception e) {
		}
		if (!lessonDateTimes.isEmpty()) {
			for (LessonDateTime dbLessonDateTime : lessonDateTimes) {
				if (dbLessonDateTime.getLesson().getLessonId() == lessonDateTime.getLesson().getLessonId()
						&& dbLessonDateTime.getDate().equals(lessonDateTime.getDate())
						&& dbLessonDateTime.getTimeFrom().equals(lessonDateTime.getTimeFrom())
						&& dbLessonDateTime.getTimeTo().equals(lessonDateTime.getTimeTo())) {
					throw new Exception("LessonDateTime already exists");
				}
			}
		}
		return lessonDateTimeRepo.save(lessonDateTime);
	}

	@Override
	public LessonDateTime updateLessonDateTimeById(int id, LessonDateTime lessonDateTime) throws Exception {
		LessonDateTime oldLessonDateTime = selectLessonDateTimeById(id);
		oldLessonDateTime.setCustom(lessonDateTime.isCustom());
		oldLessonDateTime.setDate(lessonDateTime.getDate());
		oldLessonDateTime.setTimeFrom(lessonDateTime.getTimeFrom());
		oldLessonDateTime.setTimeTo(lessonDateTime.getTimeTo());
		lessonDateTimeRepo.save(oldLessonDateTime);
		return oldLessonDateTime;
	}

	@Override
	public ArrayList<LessonDateTime> selectAllByLessonLessonId(int id) throws Exception {
		if (lessonDateTimeRepo.count() == 0)
			return new ArrayList<LessonDateTime>();
		return (ArrayList<LessonDateTime>) lessonDateTimeRepo.findByLessonLessonId(id);
	}

}
