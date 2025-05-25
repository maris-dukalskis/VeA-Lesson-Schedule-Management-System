package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import lv.venta.model.CourseStudyProgrammeAlias;
import lv.venta.repo.ICourseStudyProgrammeAliasRepo;
import lv.venta.service.ICourseStudyProgrammeAliasService;

@Service
public class CourseStudyProgrammeAliasServiceImpl implements ICourseStudyProgrammeAliasService {

	private final ICourseStudyProgrammeAliasRepo courseStudyProgrammeAliasRepo;

	public CourseStudyProgrammeAliasServiceImpl(ICourseStudyProgrammeAliasRepo courseStudyProgrammeAliasRepo) {
		this.courseStudyProgrammeAliasRepo = courseStudyProgrammeAliasRepo;
	}

	@Override
	public List<CourseStudyProgrammeAlias> selectAllCourseStudyProgrammeAliases() {
		if (courseStudyProgrammeAliasRepo.count() == 0)
			return new ArrayList<>();
		return (List<CourseStudyProgrammeAlias>) courseStudyProgrammeAliasRepo.findAll();
	}

	@Override
	public CourseStudyProgrammeAlias selectCourseStudyProgrammeAliasById(int id)
			throws IllegalArgumentException, NoSuchElementException {
		if (id < 0)
			throw new IllegalArgumentException("ID cannot be below 0");
		if (!courseStudyProgrammeAliasRepo.existsById(id)) {
			throw new NoSuchElementException("CourseStudyProgrammeAlias by that ID does not exist");
		}
		return courseStudyProgrammeAliasRepo.findById(id).get();
	}

	@Override
	public void deleteCourseStudyProgrammeAliasById(int id) {
		courseStudyProgrammeAliasRepo.delete(selectCourseStudyProgrammeAliasById(id));
	}

	@Override
	public CourseStudyProgrammeAlias insertNewCourseStudyProgrammeAlias(
			CourseStudyProgrammeAlias courseStudyProgrammeAlias) throws NullPointerException, IllegalStateException {
		if (courseStudyProgrammeAlias == null)
			throw new NullPointerException("CourseStudyProgrammeAlias object cannot be null");

		List<CourseStudyProgrammeAlias> courseStudyProgrammeAliases = selectAllCourseStudyProgrammeAliases();

		if (!courseStudyProgrammeAliases.isEmpty()) {
			for (CourseStudyProgrammeAlias dbCourseStudyProgrammeAlias : courseStudyProgrammeAliases) {
				if (dbCourseStudyProgrammeAlias.getCourse().getCourseId() == courseStudyProgrammeAlias.getCourse()
						.getCourseId()
						&& dbCourseStudyProgrammeAlias.getStudyProgramme()
								.getStudyProgrammeId() == courseStudyProgrammeAlias.getStudyProgramme()
										.getStudyProgrammeId()) {
					throw new IllegalStateException("CourseStudyProgrammeAlias already exists");
				}
			}
		}
		return courseStudyProgrammeAliasRepo.save(courseStudyProgrammeAlias);
	}

	@Override
	public CourseStudyProgrammeAlias updateCourseStudyProgrammeAliasById(int id,
			CourseStudyProgrammeAlias courseStudyProgrammeAlias) {
		CourseStudyProgrammeAlias oldCourseStudyProgrammeAlias = selectCourseStudyProgrammeAliasById(id);
		oldCourseStudyProgrammeAlias.setAlias(courseStudyProgrammeAlias.getAlias());
		oldCourseStudyProgrammeAlias.setCourse(courseStudyProgrammeAlias.getCourse());
		oldCourseStudyProgrammeAlias.setStudyProgramme(courseStudyProgrammeAlias.getStudyProgramme());
		courseStudyProgrammeAliasRepo.save(oldCourseStudyProgrammeAlias);
		return oldCourseStudyProgrammeAlias;
	}

	@Override
	public List<CourseStudyProgrammeAlias> selectByStudyProgrammeId(int id) {
		if (courseStudyProgrammeAliasRepo.count() == 0)
			return new ArrayList<>();
		return courseStudyProgrammeAliasRepo.findByStudyProgrammeStudyProgrammeId(id);
	}

	@Override
	public List<CourseStudyProgrammeAlias> selectByCourseId(int id) {
		if (courseStudyProgrammeAliasRepo.count() == 0)
			return new ArrayList<>();
		return courseStudyProgrammeAliasRepo.findByCourseCourseId(id);
	}

}
