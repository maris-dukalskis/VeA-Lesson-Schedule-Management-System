package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import lv.venta.model.CourseStudyProgrammeAlias;
import lv.venta.model.StudyProgramme;
import lv.venta.repo.IStudyProgrammeRepo;
import lv.venta.service.ICourseStudyProgrammeAliasService;
import lv.venta.service.IStudyProgrammeService;

@Service
public class StudyProgrammeServiceImpl implements IStudyProgrammeService {

	private final IStudyProgrammeRepo studyProgrammeRepo;

	private final ICourseStudyProgrammeAliasService courseStudyProgrammeAliasService;

	public StudyProgrammeServiceImpl(IStudyProgrammeRepo studyProgrammeRepo,
			ICourseStudyProgrammeAliasService courseStudyProgrammeAliasService) {
		this.studyProgrammeRepo = studyProgrammeRepo;
		this.courseStudyProgrammeAliasService = courseStudyProgrammeAliasService;
	}

	@Override
	public List<StudyProgramme> selectAllStudyProgrammes() {
		if (studyProgrammeRepo.count() == 0)
			return new ArrayList<>();
		return (List<StudyProgramme>) studyProgrammeRepo.findAll();
	}

	@Override
	public StudyProgramme selectStudyProgrammeById(int id) throws IllegalArgumentException, NoSuchElementException {
		if (id < 0)
			throw new IllegalArgumentException("ID cannot be below 0");
		if (!studyProgrammeRepo.existsById(id)) {
			throw new NoSuchElementException("StudyProgramme by that ID does not exist");
		}
		return studyProgrammeRepo.findById(id).get();
	}

	@Override
	public void deleteStudyProgrammeById(int id) {
		List<CourseStudyProgrammeAlias> courseStudyProgrammeAliases = courseStudyProgrammeAliasService
				.selectByStudyProgrammeId(id);
		if (!courseStudyProgrammeAliases.isEmpty()) {
			for (CourseStudyProgrammeAlias courseStudyProgrammeAlias : courseStudyProgrammeAliases) {
				courseStudyProgrammeAliasService.deleteCourseStudyProgrammeAliasById(
						courseStudyProgrammeAlias.getCourseStudyProgrammeAliasId());
			}

		}
		studyProgrammeRepo.delete(selectStudyProgrammeById(id));
	}

	@Override
	public StudyProgramme insertNewStudyProgramme(StudyProgramme studyProgramme)
			throws NullPointerException, IllegalStateException {
		if (studyProgramme == null)
			throw new NullPointerException("StudyProgramme object cannot be null");

		List<StudyProgramme> studyProgrammes = selectAllStudyProgrammes();

		if (!studyProgrammes.isEmpty()) {
			for (StudyProgramme dbStudyProgramme : studyProgrammes) {
				if (dbStudyProgramme.getName().equals(studyProgramme.getName())
						&& dbStudyProgramme.getYear() == studyProgramme.getYear()) {
					throw new IllegalStateException("StudyProgramme already exists");
				}
			}
		}
		return studyProgrammeRepo.save(studyProgramme);
	}

	@Override
	public StudyProgramme updateStudyProgrammeById(int id, StudyProgramme studyProgramme) {
		StudyProgramme oldStudyProgramme = selectStudyProgrammeById(id);
		oldStudyProgramme.setShortName(studyProgramme.getShortName());
		oldStudyProgramme.setName(studyProgramme.getName());
		oldStudyProgramme.setYear(studyProgramme.getYear());
		studyProgrammeRepo.save(oldStudyProgramme);
		return oldStudyProgramme;
	}

	@Override
	public List<StudyProgramme> selectByCourseId(int id) {
		if (studyProgrammeRepo.count() == 0)
			return new ArrayList<>();
		return (List<StudyProgramme>) studyProgrammeRepo.findByCourseStudyProgrammeAliasesCourseCourseId(id);
	}

}
