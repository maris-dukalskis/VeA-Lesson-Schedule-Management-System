package lv.venta.ut;

import lv.venta.model.*;
import lv.venta.repo.*;
import lv.venta.service.impl.LessonServiceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LessonServiceImplTest {

	@Autowired
	private LessonServiceImpl lessonService;

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

	private final List<Lesson> testLessons = new ArrayList<>();

	private Course course;
	private Classroom classroom;
	private Lecturer lecturer;
	private Semester semester;

	@BeforeEach
	void setupTestLessons() {
		course = courseRepo.save(new Course("Test Course", "TC", "Test Description", 5));
		classroom = classroomRepo.save(new Classroom("Test Building", 101, "Test Equipment", 30));
		lecturer = lecturerRepo
				.save(new Lecturer("Test", "lecturer@test.com", Role.LECTURER, 20, "Test Notes", "Test Seniority"));
		semester = semesterRepo.save(new Semester("Test Name", SemesterStatus.PLANNED));

		Lesson lesson1 = lessonRepo.save(new Lesson(course, lecturer, classroom, semester, 1, false, ""));
		Lesson lesson2 = lessonRepo.save(new Lesson(course, lecturer, classroom, semester, 2, true, "link"));

		testLessons.add(lesson1);
		testLessons.add(lesson2);
	}

	@AfterEach
	void cleanupTestLessons() throws Exception {
		for (Lesson lesson : testLessons) {
			if (lessonRepo.existsById(lesson.getLessonId())) {
				lessonService.deleteLessonById(lesson.getLessonId());
			}
		}
		testLessons.clear();

		if (lessonRepo.count() == 0) {
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
	}

	@Test
	void testSelectAllLessonsContainsInsertedOnes() throws Exception {
		List<Lesson> allLessons = lessonService.selectAllLessons();

		assertTrue(allLessons.stream().anyMatch(lesson -> lesson.getLessonGroup() == 1));
		assertTrue(allLessons.stream().anyMatch(lesson -> lesson.getLessonGroup() == 2));
	}

	@Test
	void testSelectLessonByIdReturnsCorrectLesson() throws Exception {
		Lesson lessonToFind = testLessons.get(0);

		Lesson foundLesson = lessonService.selectLessonById(lessonToFind.getLessonId());

		assertEquals(lessonToFind.getLessonGroup(), foundLesson.getLessonGroup());
	}

	@Test
	void testInsertNewLessonSuccess() throws Exception {
		Lesson lessonToInsert = new Lesson(course, lecturer, classroom, semester, 3, false, "link");

		Lesson insertedLesson = lessonService.insertNewLesson(lessonToInsert);

		assertNotNull(insertedLesson.getLessonId());
		assertEquals(3, insertedLesson.getLessonGroup());

		lessonService.deleteLessonById(insertedLesson.getLessonId());
	}

	@Test
	void testUpdateLessonByIdSuccess() throws Exception {
		Lesson lessonToUpdate = testLessons.get(1);
		Lesson updatedLessonData = new Lesson(course, lecturer, classroom, semester, 5, true, "Updated link");

		Lesson updatedLesson = lessonService.updateLessonById(lessonToUpdate.getLessonId(), updatedLessonData);

		assertEquals(5, updatedLesson.getLessonGroup());
		assertTrue(updatedLesson.isOnline());
		assertEquals("Updated link", updatedLesson.getOnlineInformation());
	}

	@Test
	void testDeleteLessonById() throws Exception {
		Lesson lessonToDelete = lessonRepo.save(new Lesson(course, lecturer, classroom, semester, 4, false, "link"));
		testLessons.add(lessonToDelete);

		lessonService.deleteLessonById(lessonToDelete.getLessonId());

		assertFalse(lessonRepo.existsById(lessonToDelete.getLessonId()));
		testLessons.remove(lessonToDelete);
	}
}
