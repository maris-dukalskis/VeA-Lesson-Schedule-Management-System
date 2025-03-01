package lv.venta.repo;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.CourseStudyProgrammeAlias;

public interface ICourseStudyProgrammeAliasRepo extends CrudRepository<CourseStudyProgrammeAlias, Integer> {

	ArrayList<CourseStudyProgrammeAlias> findByStudyProgrammeStudyProgrammeId(int id);

	ArrayList<CourseStudyProgrammeAlias> findByCourseCourseId(int id);

}
