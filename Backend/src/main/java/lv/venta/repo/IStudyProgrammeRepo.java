package lv.venta.repo;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.StudyProgramme;

public interface IStudyProgrammeRepo extends CrudRepository<StudyProgramme, Integer> {

	ArrayList<StudyProgramme> findByCoursesCourseId(int id);
}
