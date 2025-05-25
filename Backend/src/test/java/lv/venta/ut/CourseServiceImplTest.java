package lv.venta.ut;

import lv.venta.model.Course;
import lv.venta.repo.ICourseRepo;
import lv.venta.service.impl.CourseServiceImpl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseServiceImplTest {

	@Autowired
	private CourseServiceImpl courseService;

	@Autowired
	private ICourseRepo courseRepo;

	private final ArrayList<Course> testCourses = new ArrayList<>();

	@BeforeEach
	void setupTestCourses() {
		Course course1 = courseRepo.save(new Course("TestCourseA", "TCA", "DescriptionA", 3));
		Course course2 = courseRepo.save(new Course("TestCourseB", "TCB", "DescriptionB", 4));
		testCourses.add(course1);
		testCourses.add(course2);
	}

	@AfterEach
	void cleanupTestCourses() {
		for (Course course : testCourses) {
			if (courseRepo.existsById(course.getCourseId())) {
				courseRepo.deleteById(course.getCourseId());
			}
		}
		testCourses.clear();
	}

	@Test
	void testSelectAllCoursesContainsInsertedOnes() throws Exception {
		var allCourses = courseService.selectAllCourses();
		assertTrue(allCourses.stream().anyMatch(course -> course.getName().equals("TestCourseA")));
		assertTrue(allCourses.stream().anyMatch(course -> course.getName().equals("TestCourseB")));
	}

	@Test
	void testSelectCourseByIdReturnsCorrectCourse() throws Exception {
		Course courseToFind = testCourses.get(0);
		Course foundCourse = courseService.selectCourseById(courseToFind.getCourseId());
		assertEquals("TestCourseA", foundCourse.getName());
	}

	@Test
	void testInsertNewCourseSuccess() throws Exception {
		Course newCourse = new Course("InsertCourse", "IC", "InsertDesc", 6);
		Course insertedCourse = courseService.insertNewCourse(newCourse);
		assertNotNull(insertedCourse.getCourseId());
		assertEquals("InsertCourse", insertedCourse.getName());

		courseRepo.deleteById(insertedCourse.getCourseId());
	}

	@Test
	void testUpdateCourseByIdSuccess() throws Exception {
		Course courseToUpdate = testCourses.get(1);
		Course updateData = new Course("UpdatedName", "UPD", "UpdatedDesc", 5);

		Course updatedCourse = courseService.updateCourseById(courseToUpdate.getCourseId(), updateData);

		assertEquals("UpdatedName", updatedCourse.getName());
		assertEquals("UPD", updatedCourse.getShortName());
		assertEquals("UpdatedDesc", updatedCourse.getDescription());
		assertEquals(5, updatedCourse.getCreditPoints());
	}

	@Test
	void testDeleteCourseByIdSuccess() throws Exception {
		Course tempCourse = courseRepo.save(new Course("TempCourse", "TMP", "TempDesc", 2));
		testCourses.add(tempCourse);

		courseService.deleteCourseById(tempCourse.getCourseId());

		assertFalse(courseRepo.existsById(tempCourse.getCourseId()));
		testCourses.remove(tempCourse);
	}
}
