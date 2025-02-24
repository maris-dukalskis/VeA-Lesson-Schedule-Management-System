package lv.venta.repo;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Course;

public interface ICourseRepo extends CrudRepository<Course, Integer> {

	ArrayList<Course> findByCourseStudyProgrammeAliasesStudyProgrammeStudyProgrammeId(int id);

	ArrayList<Course> findByUsersUserId(int id);
}
