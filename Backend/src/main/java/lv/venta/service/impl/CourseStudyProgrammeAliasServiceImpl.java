package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.CourseStudyProgrammeAlias;
import lv.venta.repo.ICourseStudyProgrammeAliasRepo;
import lv.venta.service.ICourseStudyProgrammeAliasService;

@Service
public class CourseStudyProgrammeAliasServiceImpl implements ICourseStudyProgrammeAliasService {

	@Autowired
	private ICourseStudyProgrammeAliasRepo courseStudyProgrammeAliasRepo;

	@Override
	public ArrayList<CourseStudyProgrammeAlias> selectAllCourseStudyProgrammeAliases() throws Exception {
		if (courseStudyProgrammeAliasRepo.count() == 0)
			return new ArrayList<CourseStudyProgrammeAlias>();
		return (ArrayList<CourseStudyProgrammeAlias>) courseStudyProgrammeAliasRepo.findAll();
	}

	@Override
	public CourseStudyProgrammeAlias selectCourseStudyProgrammeAliasById(int id) throws Exception {
		if (id < 0)
			throw new Exception("ID cannot be below 0");
		if (!courseStudyProgrammeAliasRepo.existsById(id)) {
			throw new Exception("CourseStudyProgrammeAlias by that ID does not exist");
		}
		return courseStudyProgrammeAliasRepo.findById(id).get();
	}

	@Override
	public void deleteCourseStudyProgrammeAliasById(int id) throws Exception {
		courseStudyProgrammeAliasRepo.delete(selectCourseStudyProgrammeAliasById(id));
	}

	@Override
	public CourseStudyProgrammeAlias insertNewCourseStudyProgrammeAlias(
			CourseStudyProgrammeAlias courseStudyProgrammeAlias) throws Exception {
		if (courseStudyProgrammeAlias == null)
			throw new Exception("CourseStudyProgrammeAlias object cannot be null");
		List<CourseStudyProgrammeAlias> courseStudyProgrammeAliases = new ArrayList<>();
		try {
			courseStudyProgrammeAliases = selectAllCourseStudyProgrammeAliases();
		} catch (Exception e) {
		}
		if (!courseStudyProgrammeAliases.isEmpty()) {
			for (CourseStudyProgrammeAlias dbCourseStudyProgrammeAlias : courseStudyProgrammeAliases) {
				if (dbCourseStudyProgrammeAlias.getCourse().getCourseId() == courseStudyProgrammeAlias.getCourse()
						.getCourseId()
						&& dbCourseStudyProgrammeAlias.getStudyProgramme()
								.getStudyProgrammeId() == courseStudyProgrammeAlias.getStudyProgramme()
										.getStudyProgrammeId()) {
					throw new Exception("CourseStudyProgrammeAlias already exists");
				}
			}
		}
		return courseStudyProgrammeAliasRepo.save(courseStudyProgrammeAlias);
	}

	@Override
	public CourseStudyProgrammeAlias updateCourseStudyProgrammeAliasById(int id,
			CourseStudyProgrammeAlias courseStudyProgrammeAlias) throws Exception {
		CourseStudyProgrammeAlias oldCourseStudyProgrammeAlias = selectCourseStudyProgrammeAliasById(id);
		oldCourseStudyProgrammeAlias.setAlias(courseStudyProgrammeAlias.getAlias());
		oldCourseStudyProgrammeAlias.setCourse(courseStudyProgrammeAlias.getCourse());
		oldCourseStudyProgrammeAlias.setStudyProgramme(courseStudyProgrammeAlias.getStudyProgramme());
		courseStudyProgrammeAliasRepo.save(oldCourseStudyProgrammeAlias);
		return oldCourseStudyProgrammeAlias;
	}

	@Override
	public ArrayList<CourseStudyProgrammeAlias> selectByStudyProgrammeId(int id) throws Exception {
		if (courseStudyProgrammeAliasRepo.count() == 0)
			return new ArrayList<CourseStudyProgrammeAlias>();
		return (ArrayList<CourseStudyProgrammeAlias>) courseStudyProgrammeAliasRepo
				.findByStudyProgrammeStudyProgrammeId(id);
	}

	@Override
	public ArrayList<CourseStudyProgrammeAlias> selectByCourseId(int id) throws Exception {
		if (courseStudyProgrammeAliasRepo.count() == 0)
			return new ArrayList<CourseStudyProgrammeAlias>();
		return (ArrayList<CourseStudyProgrammeAlias>) courseStudyProgrammeAliasRepo.findByCourseCourseId(id);
	}

}
