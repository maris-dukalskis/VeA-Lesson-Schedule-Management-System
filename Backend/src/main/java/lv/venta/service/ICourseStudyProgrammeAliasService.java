package lv.venta.service;

import java.util.List;
import java.util.NoSuchElementException;

import lv.venta.model.CourseStudyProgrammeAlias;

public interface ICourseStudyProgrammeAliasService {

	public abstract List<CourseStudyProgrammeAlias> selectAllCourseStudyProgrammeAliases();

	public abstract CourseStudyProgrammeAlias selectCourseStudyProgrammeAliasById(int id)
			throws IllegalArgumentException, NoSuchElementException;

	public abstract void deleteCourseStudyProgrammeAliasById(int id);

	public abstract CourseStudyProgrammeAlias insertNewCourseStudyProgrammeAlias(
			CourseStudyProgrammeAlias courseStudyProgrammeAlias) throws NullPointerException, IllegalStateException;

	public abstract CourseStudyProgrammeAlias updateCourseStudyProgrammeAliasById(int id,
			CourseStudyProgrammeAlias courseStudyProgrammeAlias);

	public abstract List<CourseStudyProgrammeAlias> selectByStudyProgrammeId(int id);

	public abstract List<CourseStudyProgrammeAlias> selectByCourseId(int id);

}
