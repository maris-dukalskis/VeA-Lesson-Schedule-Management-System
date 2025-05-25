package lv.venta.ut;

import lv.venta.model.*;
import lv.venta.repo.*;
import lv.venta.service.impl.LessonDateTimeServiceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LessonDateTimeServiceImplTest {

	@Autowired
	private LessonDateTimeServiceImpl lessonDateTimeService;

	@Autowired
	private ILessonDateTimeRepo lessonDateTimeRepo;

	@Autowired
	private ILessonRepo lessonRepo;

	@Autowired
	private ICourseRepo courseRepo;

	@Autowired
	private IClassroomRepo classroomRepo;

	@Autowired
	private ILecturerRepo lecturerRepo;

	@Autowired
	private ISemesterRepo semesterRepo;

	private final List<LessonDateTime> testLessonDateTimes = new ArrayList<>();
	private final List<Lesson> testLessons = new ArrayList<>();

	private Course course;
	private Classroom classroom;
	private Lecturer lecturer;
	private Semester semester;

	@BeforeEach
	public void setupTestLessonDateTimes() throws Exception {
		course = courseRepo.save(new Course("Test Course", "TC", "Test Description", 5));
		classroom = classroomRepo.save(new Classroom("Test Building", 101, "Test Equipment", 30));
		lecturer = lecturerRepo
				.save(new Lecturer("Test", "lecturer@test.com", Role.LECTURER, 20, "Test Notes", "Test Seniority"));
		semester = semesterRepo.save(new Semester("Test Name", SemesterStatus.PLANNED));

		Lesson lesson = lessonRepo.save(new Lesson(course, lecturer, classroom, semester, 1, false, ""));
		testLessons.add(lesson);

		LessonDateTime ldt1 = lessonDateTimeRepo
				.save(new LessonDateTime(lesson, false, Date.valueOf("2024-05-01"), "10:00", "12:00"));
		LessonDateTime ldt2 = lessonDateTimeRepo
				.save(new LessonDateTime(lesson, true, Date.valueOf("2024-05-02"), "14:00", "16:00"));

		testLessonDateTimes.add(ldt1);
		testLessonDateTimes.add(ldt2);
	}

	@AfterEach
	public void cleanupTestLessonDateTimes() throws Exception {
		for (LessonDateTime ldt : testLessonDateTimes) {
			if (lessonDateTimeRepo.existsById(ldt.getLessonDateTimeId())) {
				lessonDateTimeService.deleteLessonDateTimeById(ldt.getLessonDateTimeId());
			}
		}
		testLessonDateTimes.clear();

		for (Lesson lesson : testLessons) {
			if (lessonRepo.existsById(lesson.getLessonId())) {
				lessonRepo.deleteById(lesson.getLessonId());
			}
		}
		testLessons.clear();

		if (semesterRepo.existsById(semester.getSemesterId())) {
			semesterRepo.deleteById(semester.getSemesterId());
		}
		if (lecturerRepo.existsById(lecturer.getUserId())) {
			lecturerRepo.deleteById(lecturer.getUserId());
		}
		if (classroomRepo.existsById(classroom.getClassroomId())) {
			classroomRepo.deleteById(classroom.getClassroomId());
		}
		if (courseRepo.existsById(course.getCourseId())) {
			courseRepo.deleteById(course.getCourseId());
		}
	}

	@Test
	void testSelectAllLessonDateTimesContainsInsertedOnes() throws Exception {
		List<LessonDateTime> allLessonDateTimes = lessonDateTimeService.selectAllLessonDateTimes();

		assertTrue(allLessonDateTimes.stream().anyMatch(ldt -> ldt.getTimeFrom().equals("10:00")));
		assertTrue(allLessonDateTimes.stream().anyMatch(ldt -> ldt.getTimeFrom().equals("14:00")));
	}

	@Test
	void testSelectLessonDateTimeByIdReturnsCorrectLessonDateTime() throws Exception {
		LessonDateTime ldtToFind = testLessonDateTimes.get(0);

		LessonDateTime foundLdt = lessonDateTimeService.selectLessonDateTimeById(ldtToFind.getLessonDateTimeId());

		assertEquals(ldtToFind.getTimeFrom(), foundLdt.getTimeFrom());
		assertEquals(ldtToFind.getDate(), foundLdt.getDate());
	}

	@Test
	void testInsertNewLessonDateTimeSuccess() throws Exception {
		Lesson lesson = testLessons.get(0);
		LessonDateTime ldtToInsert = new LessonDateTime(lesson, false, Date.valueOf("2024-05-03"), "08:00", "10:00");

		LessonDateTime insertedLdt = lessonDateTimeService.insertNewLessonDateTime(ldtToInsert);

		assertNotNull(insertedLdt.getLessonDateTimeId());
		assertEquals("08:00", insertedLdt.getTimeFrom());

		lessonDateTimeService.deleteLessonDateTimeById(insertedLdt.getLessonDateTimeId());
	}

	@Test
	void testUpdateLessonDateTimeByIdSuccess() throws Exception {
		LessonDateTime ldtToUpdate = testLessonDateTimes.get(1);
		LessonDateTime newData = new LessonDateTime(ldtToUpdate.getLesson(), false, Date.valueOf("2024-06-01"), "09:00",
				"11:00");

		LessonDateTime updatedLdt = lessonDateTimeService.updateLessonDateTimeById(ldtToUpdate.getLessonDateTimeId(),
				newData);

		assertEquals("09:00", updatedLdt.getTimeFrom());
		assertEquals("11:00", updatedLdt.getTimeTo());
		assertEquals(Date.valueOf("2024-06-01"), updatedLdt.getDate());
	}

	@Test
	void testDeleteLessonDateTimeById() throws Exception {
		Lesson lesson = testLessons.get(0);
		LessonDateTime ldtToDelete = lessonDateTimeRepo
				.save(new LessonDateTime(lesson, true, Date.valueOf("2024-05-04"), "12:00", "14:00"));
		testLessonDateTimes.add(ldtToDelete);

		lessonDateTimeService.deleteLessonDateTimeById(ldtToDelete.getLessonDateTimeId());

		assertFalse(lessonDateTimeRepo.existsById(ldtToDelete.getLessonDateTimeId()));
		testLessonDateTimes.remove(ldtToDelete);
	}
}
