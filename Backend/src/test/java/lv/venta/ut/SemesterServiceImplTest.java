package lv.venta.ut;

import lv.venta.model.*;
import lv.venta.repo.*;
import lv.venta.service.impl.SemesterServiceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SemesterServiceImplTest {

	@Autowired
	private SemesterServiceImpl semesterService;

	@Autowired
	private ISemesterRepo semesterRepo;

	private final List<Semester> testSemesters = new ArrayList<>();

	@BeforeEach
	public void setupTestSemesters() {
		Semester semester1 = semesterRepo.save(new Semester("Test1", SemesterStatus.PLANNED));
		Semester semester2 = semesterRepo.save(new Semester("Test2", SemesterStatus.ONGOING));

		testSemesters.add(semester1);
		testSemesters.add(semester2);
	}

	@AfterEach
	public void cleanupTestSemesters() throws Exception {
		for (Semester semester : testSemesters) {
			if (semesterRepo.existsById(semester.getSemesterId())) {
				semesterService.deleteSemesterById(semester.getSemesterId());
			}
		}
		testSemesters.clear();
	}

	@Test
	void testSelectAllSemestersContainsInsertedOnes() throws Exception {
		List<Semester> allSemesters = semesterService.selectAllSemesters();

		assertTrue(allSemesters.stream().anyMatch(semester -> semester.getName().equals("Test1")));
		assertTrue(allSemesters.stream().anyMatch(semester -> semester.getName().equals("Test2")));
	}

	@Test
	void testSelectSemesterByIdReturnsCorrectSemester() throws Exception {
		Semester semesterToFind = testSemesters.get(0);

		Semester foundSemester = semesterService.selectSemesterById(semesterToFind.getSemesterId());

		assertEquals(semesterToFind.getName(), foundSemester.getName());
		assertEquals(semesterToFind.getSemesterStatus(), foundSemester.getSemesterStatus());
	}

	@Test
	void testInsertNewSemesterSuccess() throws Exception {
		Semester semesterToInsert = new Semester("Test3", SemesterStatus.PLANNED);

		Semester insertedSemester = semesterService.insertNewSemester(semesterToInsert);

		assertNotNull(insertedSemester.getSemesterId());
		assertEquals("Test3", insertedSemester.getName());

		semesterService.deleteSemesterById(insertedSemester.getSemesterId());
	}

	@Test
	void testUpdateSemesterByIdSuccess() throws Exception {
		Semester semesterToUpdate = testSemesters.get(1);
		Semester updatedSemesterData = new Semester("Test4", SemesterStatus.ENDED);

		Semester updatedSemester = semesterService.updateSemesterById(semesterToUpdate.getSemesterId(),
				updatedSemesterData);

		assertEquals("Test4", updatedSemester.getName());
		assertEquals(SemesterStatus.ENDED, updatedSemester.getSemesterStatus());
	}

	@Test
	void testDeleteSemesterById() throws Exception {
		Semester semesterToDelete = semesterRepo.save(new Semester("Temporary Semester", SemesterStatus.PLANNED));
		testSemesters.add(semesterToDelete);

		semesterService.deleteSemesterById(semesterToDelete.getSemesterId());

		assertFalse(semesterRepo.existsById(semesterToDelete.getSemesterId()));
		testSemesters.remove(semesterToDelete);
	}
}
