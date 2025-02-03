package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.StudyProgramme;
import lv.venta.repo.IStudyProgrammeRepo;
import lv.venta.service.IStudyProgrammeService;

@Service
public class StudyProgrammeServiceImpl implements IStudyProgrammeService {

	@Autowired
	IStudyProgrammeRepo studyProgrammeRepo;

	@Override
	public ArrayList<StudyProgramme> selectAllStudyProgrammes() throws Exception {
		if (studyProgrammeRepo.count() == 0)
			throw new Exception("StudyProgramme table is empty");
		return (ArrayList<StudyProgramme>) studyProgrammeRepo.findAll();
	}

	@Override
	public StudyProgramme selectStudyProgrammeById(int id) throws Exception {
		if (id < 0)
			throw new Exception("ID cannot be below 0");
		if (!studyProgrammeRepo.existsById(id)) {
			throw new Exception("StudyProgramme by that ID does not exist");
		}
		return studyProgrammeRepo.findById(id).get();
	}

	@Override
	public void deleteStudyProgrammeById(int id) throws Exception {
		studyProgrammeRepo.delete(selectStudyProgrammeById(id));
	}

	@Override
	public StudyProgramme insertNewStudyProgramme(StudyProgramme studyProgramme) throws Exception {
		if (studyProgramme == null)
			throw new Exception("StudyProgramme object cannot be null");
		List<StudyProgramme> studyProgrammes = selectAllStudyProgrammes();
		if (!studyProgrammes.isEmpty()) {
			for (StudyProgramme dbStudyProgramme : studyProgrammes) {
				if (dbStudyProgramme.getName().equals(studyProgramme.getName())
						&& dbStudyProgramme.getYear() == studyProgramme.getYear()) {
					throw new Exception("StudyProgramme already exists");
				}
			}
		}
		return studyProgrammeRepo.save(studyProgramme);
	}

	@Override
	public StudyProgramme updateStudyProgrammeById(int id, StudyProgramme studyProgramme) throws Exception {
		StudyProgramme oldStudyProgramme = selectStudyProgrammeById(id);
		oldStudyProgramme.setName(studyProgramme.getName());
		oldStudyProgramme.setIndividual(studyProgramme.isIndividual());
		oldStudyProgramme.setYear(studyProgramme.getYear());
		studyProgrammeRepo.save(oldStudyProgramme);
		return oldStudyProgramme;
	}

}
