package lv.venta.ut;

import lv.venta.model.StudyProgramme;
import lv.venta.repo.IStudyProgrammeRepo;
import lv.venta.service.IStudyProgrammeService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StudyProgrammeServiceImplTest {

	@Autowired
	private IStudyProgrammeService studyProgrammeService;

	@Autowired
	private IStudyProgrammeRepo studyProgrammeRepo;

	private final List<StudyProgramme> testStudyProgrammes = new ArrayList<>();

	@BeforeEach
	public void setupTestStudyProgrammes() throws Exception {
		StudyProgramme studyProgramme1 = studyProgrammeRepo.save(new StudyProgramme("SP1", "Study Programme 1", 1));
		StudyProgramme studyProgramme2 = studyProgrammeRepo.save(new StudyProgramme("SP2", "Study Programme 2", 2));

		testStudyProgrammes.add(studyProgramme1);
		testStudyProgrammes.add(studyProgramme2);
	}

	@AfterEach
	public void cleanupTestStudyProgrammes() throws Exception {
		for (StudyProgramme studyProgramme : testStudyProgrammes) {
			if (studyProgrammeRepo.existsById(studyProgramme.getStudyProgrammeId())) {
				studyProgrammeService.deleteStudyProgrammeById(studyProgramme.getStudyProgrammeId());
			}
		}
		testStudyProgrammes.clear();
	}

	@Test
	void testSelectAllStudyProgrammesContainsInsertedOnes() throws Exception {
		List<StudyProgramme> allStudyProgrammes = studyProgrammeService.selectAllStudyProgrammes();

		assertTrue(allStudyProgrammes.stream().anyMatch(programme -> programme.getShortName().equals("SP1")));
		assertTrue(allStudyProgrammes.stream().anyMatch(programme -> programme.getShortName().equals("SP2")));
	}

	@Test
	void testSelectStudyProgrammeByIdReturnsCorrectStudyProgramme() throws Exception {
		StudyProgramme expectedStudyProgramme = testStudyProgrammes.get(0);

		StudyProgramme actualStudyProgramme = studyProgrammeService
				.selectStudyProgrammeById(expectedStudyProgramme.getStudyProgrammeId());

		assertEquals(expectedStudyProgramme.getShortName(), actualStudyProgramme.getShortName());
		assertEquals(expectedStudyProgramme.getName(), actualStudyProgramme.getName());
		assertEquals(expectedStudyProgramme.getYear(), actualStudyProgramme.getYear());
	}

	@Test
	void testInsertNewStudyProgrammeSuccess() throws Exception {
		StudyProgramme newStudyProgramme = new StudyProgramme("SP3", "Study Programme 3", 3);

		StudyProgramme insertedStudyProgramme = studyProgrammeService.insertNewStudyProgramme(newStudyProgramme);

		assertNotNull(insertedStudyProgramme.getStudyProgrammeId());
		assertEquals("SP3", insertedStudyProgramme.getShortName());

		testStudyProgrammes.add(insertedStudyProgramme);
	}

	@Test
	void testUpdateStudyProgrammeByIdSuccess() throws Exception {
		StudyProgramme existingStudyProgramme = testStudyProgrammes.get(1);

		StudyProgramme updatedStudyProgrammeData = new StudyProgramme("UpdatedSP", "Updated Programme Name", 4);

		StudyProgramme updatedStudyProgramme = studyProgrammeService
				.updateStudyProgrammeById(existingStudyProgramme.getStudyProgrammeId(), updatedStudyProgrammeData);

		assertEquals("UpdatedSP", updatedStudyProgramme.getShortName());
		assertEquals("Updated Programme Name", updatedStudyProgramme.getName());
		assertEquals(4, updatedStudyProgramme.getYear());
	}

	@Test
	void testDeleteStudyProgrammeById() throws Exception {
		StudyProgramme studyProgrammeToDelete = studyProgrammeRepo
				.save(new StudyProgramme("SPDel", "Programme To Delete", 5));

		testStudyProgrammes.add(studyProgrammeToDelete);

		studyProgrammeService.deleteStudyProgrammeById(studyProgrammeToDelete.getStudyProgrammeId());

		assertFalse(studyProgrammeRepo.existsById(studyProgrammeToDelete.getStudyProgrammeId()));
		testStudyProgrammes.remove(studyProgrammeToDelete);
	}
}
