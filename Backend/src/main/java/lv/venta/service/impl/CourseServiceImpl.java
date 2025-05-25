package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import lv.venta.model.Course;
import lv.venta.model.Lesson;
import lv.venta.model.User;
import lv.venta.repo.ICourseRepo;
import lv.venta.repo.ILessonRepo;
import lv.venta.repo.IUserRepo;
import lv.venta.service.ICourseService;
import lv.venta.service.ILessonService;
import lv.venta.service.IUserService;

@Service
public class CourseServiceImpl implements ICourseService {

	private final ICourseRepo courseRepo;

	private final IUserService userService;

	private final IUserRepo userRepo;

	private final ILessonService lessonService;

	private final ILessonRepo lessonRepo;

	public CourseServiceImpl(ICourseRepo courseRepo, IUserService userService, IUserRepo userRepo,
			ILessonService lessonService, ILessonRepo lessonRepo) {
		this.courseRepo = courseRepo;
		this.userService = userService;
		this.userRepo = userRepo;
		this.lessonService = lessonService;
		this.lessonRepo = lessonRepo;
	}

	@Override
	public List<Course> selectAllCourses() {
		if (courseRepo.count() == 0)
			return new ArrayList<>();
		return (List<Course>) courseRepo.findAll();
	}

	@Override
	public Course selectCourseById(int id) throws IllegalArgumentException, NoSuchElementException {
		if (id < 0)
			throw new IllegalArgumentException("ID cannot be below 0");
		if (!courseRepo.existsById(id)) {
			throw new NoSuchElementException("Course by that ID does not exist");
		}
		return courseRepo.findById(id).get();
	}

	@Override
	public void deleteCourseById(int id) {

		List<User> users = userService.selectByCourseId(id);
		if (!users.isEmpty()) {
			Course course = selectCourseById(id);
			for (User user : users) {
				user.removeCourse(course);
			}
			userRepo.saveAll(users);
		}

		List<Lesson> lessons = lessonService.selectByCourseId(id);
		if (!lessons.isEmpty()) {
			for (Lesson lesson : lessons) {
				lesson.setCourse(null);
			}
			lessonRepo.saveAll(lessons);
		}

		courseRepo.delete(selectCourseById(id));
	}

	@Override
	public Course insertNewCourse(Course course) throws NullPointerException, IllegalStateException {
		if (course == null)
			throw new NullPointerException("Course object cannot be null");

		List<Course> courses = selectAllCourses();

		if (!courses.isEmpty()) {
			for (Course dbCourse : courses) {
				if (dbCourse.getName().equals(course.getName())) {
					throw new IllegalStateException("Course already exists");
				}
			}
		}
		return courseRepo.save(course);
	}

	@Override
	public Course updateCourseById(int id, Course course) {
		Course oldCourse = selectCourseById(id);
		oldCourse.setDescription(course.getDescription());
		oldCourse.setShortName(course.getShortName());
		oldCourse.setName(course.getName());
		oldCourse.setCreditPoints(course.getCreditPoints());
		courseRepo.save(oldCourse);
		return oldCourse;
	}

	@Override
	public List<Course> selectByStudyProgrammeId(int id) {
		if (courseRepo.count() == 0)
			return new ArrayList<>();
		return (List<Course>) courseRepo.findByCourseStudyProgrammeAliasesStudyProgrammeStudyProgrammeId(id);
	}

	@Override
	public List<Course> selectByUserId(int id) {
		if (courseRepo.count() == 0)
			return new ArrayList<>();
		return (List<Course>) courseRepo.findByUsersUserId(id);
	}

}
