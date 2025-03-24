package lv.venta.repo;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Lesson;

public interface ILessonRepo extends CrudRepository<Lesson, Integer> {

	ArrayList<Lesson> findByCourseCourseStudyProgrammeAliasesStudyProgrammeNameAndCourseCourseStudyProgrammeAliasesStudyProgrammeYear(
			String name, int year);

	ArrayList<Lesson> findByLecturerFullName(String name);

	ArrayList<Lesson> findByClassroomBuildingAndClassroomNumber(String building, int number);

	ArrayList<Lesson> findByClassroomClassroomId(int id);

	ArrayList<Lesson> findByLecturerUserId(int id);

	ArrayList<Lesson> findByCourseCourseId(int id);

	ArrayList<Lesson> findBySemesterSemesterId(int id);

}
