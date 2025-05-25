package lv.venta.ut;

import lv.venta.model.Lecturer;
import lv.venta.model.Role;
import lv.venta.repo.ILecturerRepo;
import lv.venta.service.impl.LecturerServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LecturerServiceImplTest {

	@Autowired
	private LecturerServiceImpl lecturerService;

	@Autowired
	private ILecturerRepo lecturerRepo;

	private final List<Lecturer> testLecturers = new ArrayList<>();

	@BeforeEach
	public void setupTestLecturers() {
		Lecturer lecturer1 = lecturerRepo.save(
				new Lecturer("Test Lecturer A", "lectA@test.com", Role.LECTURER, 10, "Notes A", "Test seniority1"));
		Lecturer lecturer2 = lecturerRepo.save(
				new Lecturer("Test Lecturer B", "lectB@test.com", Role.LECTURER, 15, "Notes B", "Test seniority2"));
		testLecturers.add(lecturer1);
		testLecturers.add(lecturer2);
	}

	@AfterEach
	public void cleanupTestLecturers() {
		for (Lecturer lecturer : testLecturers) {
			if (lecturerRepo.existsById(lecturer.getUserId())) {
				lecturerRepo.deleteById(lecturer.getUserId());
			}
		}
		testLecturers.clear();
	}

	@Test
	void testSelectAllLecturersContainsInsertedOnes() throws Exception {
		List<Lecturer> allLecturers = lecturerService.selectAllLecturers();

		assertTrue(allLecturers.stream().anyMatch(lecturer -> lecturer.getFullName().equals("Test Lecturer A")));
		assertTrue(allLecturers.stream().anyMatch(lecturer -> lecturer.getFullName().equals("Test Lecturer B")));
	}

	@Test
	void testSelectLecturerByIdReturnsCorrectLecturer() throws Exception {
		Lecturer lecturerToFind = testLecturers.get(0);

		Lecturer foundLecturer = lecturerService.selectLecturerById(lecturerToFind.getUserId());

		assertEquals("Test Lecturer A", foundLecturer.getFullName());
	}

	@Test
	void testInsertNewLecturerSuccess() throws Exception {
		Lecturer lecturerToInsert = new Lecturer("Inserted Lecturer", "insert@test.com", Role.LECTURER, 20,
				"Insert Notes", "Test seniority3");

		Lecturer insertedLecturer = lecturerService.insertNewLecturer(lecturerToInsert);

		assertNotNull(insertedLecturer.getUserId());
		assertEquals("Inserted Lecturer", insertedLecturer.getFullName());

		lecturerRepo.deleteById(insertedLecturer.getUserId());
	}

	@Test
	void testUpdateLecturerByIdSuccess() throws Exception {
		Lecturer lecturerToUpdate = testLecturers.get(1);
		Lecturer updatedLecturerData = new Lecturer("Updated Lecturer", "updated@test.com", Role.LECTURER, 25,
				"Updated Notes", "Test seniority4");

		Lecturer updatedLecturer = lecturerService.updateLecturerById(lecturerToUpdate.getUserId(),
				updatedLecturerData);

		assertEquals("Updated Lecturer", updatedLecturer.getFullName());
		assertEquals("updated@test.com", updatedLecturer.getEmail());
		assertEquals(25, updatedLecturer.getHours());
		assertEquals("Updated Notes", updatedLecturer.getNotes());
		assertEquals("Test seniority4", updatedLecturer.getSeniority());
	}

	@Test
	void testDeleteLecturerById() throws Exception {
		Lecturer lecturerToDelete = lecturerRepo.save(new Lecturer("TempDelete Lecturer", "tempdelete@test.com",
				Role.LECTURER, 12, "Delete Notes", "Test seniority5"));
		testLecturers.add(lecturerToDelete);

		lecturerService.deleteLecturerById(lecturerToDelete.getUserId());

		assertFalse(lecturerRepo.existsById(lecturerToDelete.getUserId()));
		testLecturers.remove(lecturerToDelete);
	}
}
