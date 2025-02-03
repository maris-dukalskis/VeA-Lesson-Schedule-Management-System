package lv.venta.repo;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.StudyProgramme;

public interface IStudyProgrammeRepo extends CrudRepository<StudyProgramme, Integer> {

}
