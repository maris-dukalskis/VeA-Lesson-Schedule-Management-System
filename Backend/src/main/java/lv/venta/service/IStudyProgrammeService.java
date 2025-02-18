package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.StudyProgramme;

public interface IStudyProgrammeService {

	public abstract ArrayList<StudyProgramme> selectAllStudyProgrammes() throws Exception;

	public abstract StudyProgramme selectStudyProgrammeById(int id) throws Exception;

	public abstract void deleteStudyProgrammeById(int id) throws Exception;

	public abstract StudyProgramme insertNewStudyProgramme(StudyProgramme studyProgramme) throws Exception;

	public abstract StudyProgramme updateStudyProgrammeById(int id, StudyProgramme studyProgramme) throws Exception;

	public abstract ArrayList<StudyProgramme> selectByCourseId(int id) throws Exception;
}
