package lv.venta.service;

import java.util.List;
import java.util.NoSuchElementException;

import lv.venta.model.StudyProgramme;

public interface IStudyProgrammeService {

	public abstract List<StudyProgramme> selectAllStudyProgrammes();

	public abstract StudyProgramme selectStudyProgrammeById(int id)
			throws IllegalArgumentException, NoSuchElementException;

	public abstract void deleteStudyProgrammeById(int id);

	public abstract StudyProgramme insertNewStudyProgramme(StudyProgramme studyProgramme)
			throws NullPointerException, IllegalStateException;

	public abstract StudyProgramme updateStudyProgrammeById(int id, StudyProgramme studyProgramme);

	public abstract List<StudyProgramme> selectByCourseId(int id);
}
