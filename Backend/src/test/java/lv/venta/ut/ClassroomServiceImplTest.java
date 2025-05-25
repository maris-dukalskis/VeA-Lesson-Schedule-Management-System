package lv.venta.ut;

import lv.venta.model.Classroom;
import lv.venta.repo.IClassroomRepo;
import lv.venta.service.impl.ClassroomServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ClassroomServiceImplTest {

	@Autowired
	private ClassroomServiceImpl classroomService;

	@Autowired
	private IClassroomRepo classroomRepo;

	private final List<Classroom> testClassrooms = new ArrayList<>();

	@BeforeEach
	public void setupTestClassrooms() {
		Classroom classroom1 = classroomRepo.save(new Classroom("TestBuildingA", 9001, "TestDescriptionA", 20));
		Classroom classroom2 = classroomRepo.save(new Classroom("TestBuildingB", 9002, "TestDescriptionB", 30));
		testClassrooms.add(classroom1);
		testClassrooms.add(classroom2);
	}

	@AfterEach
	public void cleanupTestClassrooms() {
		for (Classroom classroom : testClassrooms) {
			if (classroomRepo.existsById(classroom.getClassroomId())) {
				classroomRepo.deleteById(classroom.getClassroomId());
			}
		}
		testClassrooms.clear();
	}

	@Test
	void testSelectAllClassroomsContainsInsertedOnes() throws Exception {
		List<Classroom> allClassrooms = classroomService.selectAllClassrooms();

		assertTrue(allClassrooms.stream().anyMatch(classroom -> classroom.getBuilding().equals("TestBuildingA")));
		assertTrue(allClassrooms.stream().anyMatch(classroom -> classroom.getBuilding().equals("TestBuildingB")));
	}

	@Test
	void testSelectClassroomByIdReturnsCorrectClassroom() throws Exception {
		Classroom classroomToFind = testClassrooms.get(0);

		Classroom foundClassroom = classroomService.selectClassroomById(classroomToFind.getClassroomId());

		assertEquals("TestBuildingA", foundClassroom.getBuilding());
	}

	@Test
	void testInsertNewClassroomSuccess() throws Exception {
		Classroom classroomToInsert = new Classroom("InsertBuilding", 9999, "InsertDescription", 50);

		Classroom insertedClassroom = classroomService.insertNewClassroom(classroomToInsert);

		assertNotNull(insertedClassroom.getClassroomId());
		assertEquals("InsertBuilding", insertedClassroom.getBuilding());

		classroomRepo.deleteById(insertedClassroom.getClassroomId());
	}

	@Test
	void testUpdateClassroomByIdSuccess() throws Exception {
		Classroom classroomToUpdate = testClassrooms.get(1);
		Classroom updatedClassroomData = new Classroom("UpdatedBuilding", 9998, "UpdatedDescription", 35);

		Classroom updatedClassroom = classroomService.updateClassroomById(classroomToUpdate.getClassroomId(),
				updatedClassroomData);

		assertEquals("UpdatedBuilding", updatedClassroom.getBuilding());
		assertEquals(9998, updatedClassroom.getNumber());
		assertEquals("UpdatedDescription", updatedClassroom.getEquipmentDescription());
		assertEquals(35, updatedClassroom.getSeats());
	}

	@Test
	void testDeleteClassroomById() throws Exception {
		Classroom classroomToDelete = classroomRepo.save(new Classroom("TempDeleteBuilding", 9003, "DeleteDesc", 25));
		testClassrooms.add(classroomToDelete);

		classroomService.deleteClassroomById(classroomToDelete.getClassroomId());

		assertFalse(classroomRepo.existsById(classroomToDelete.getClassroomId()));
		testClassrooms.remove(classroomToDelete);
	}
}
