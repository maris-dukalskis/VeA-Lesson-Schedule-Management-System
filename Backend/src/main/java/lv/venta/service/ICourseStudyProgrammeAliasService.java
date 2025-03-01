package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.CourseStudyProgrammeAlias;

public interface ICourseStudyProgrammeAliasService {

	public abstract ArrayList<CourseStudyProgrammeAlias> selectAllCourseStudyProgrammeAliases() throws Exception;

	public abstract CourseStudyProgrammeAlias selectCourseStudyProgrammeAliasById(int id) throws Exception;

	public abstract void deleteCourseStudyProgrammeAliasById(int id) throws Exception;

	public abstract CourseStudyProgrammeAlias insertNewCourseStudyProgrammeAlias(
			CourseStudyProgrammeAlias courseStudyProgrammeAlias) throws Exception;

	public abstract CourseStudyProgrammeAlias updateCourseStudyProgrammeAliasById(int id,
			CourseStudyProgrammeAlias courseStudyProgrammeAlias) throws Exception;

	public abstract ArrayList<CourseStudyProgrammeAlias> selectByStudyProgrammeId(int id) throws Exception;

	public abstract ArrayList<CourseStudyProgrammeAlias> selectByCourseId(int id) throws Exception;

}
