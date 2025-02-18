package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private ICourseRepo courseRepo;

	@Autowired
	private IUserService userService;

	@Autowired
	private IUserRepo userRepo;

	@Autowired
	private ILessonService lessonService;

	@Autowired
	private ILessonRepo lessonRepo;

	@Override
	public ArrayList<Course> selectAllCourses() throws Exception {
		if (courseRepo.count() == 0)
			return new ArrayList<Course>();
		return (ArrayList<Course>) courseRepo.findAll();
	}

	@Override
	public Course selectCourseById(int id) throws Exception {
		if (id < 0)
			throw new Exception("ID cannot be below 0");
		if (!courseRepo.existsById(id)) {
			throw new Exception("Course by that ID does not exist");
		}
		return courseRepo.findById(id).get();
	}

	@Override
	public void deleteCourseById(int id) throws Exception {

		ArrayList<User> users = userService.selectByCourseId(id);
		if (!users.isEmpty()) {
			Course course = selectCourseById(id);
			for (User user : users) {
				user.removeCourse(course);
			}
			userRepo.saveAll(users);
		}

		ArrayList<Lesson> lessons = lessonService.selectByCourseId(id);
		if (!lessons.isEmpty()) {
			for (Lesson lesson : lessons) {
				lesson.setCourse(null);
			}
			lessonRepo.saveAll(lessons);
		}

		courseRepo.delete(selectCourseById(id));
	}

	@Override
	public Course insertNewCourse(Course course) throws Exception {
		if (course == null)
			throw new Exception("Course object cannot be null");
		List<Course> courses = new ArrayList<>();
		try {
			courses = selectAllCourses();
		} catch (Exception e) {
		}
		if (!courses.isEmpty()) {
			for (Course dbCourse : courses) {
				if (dbCourse.getName().equals(course.getName())) {
					throw new Exception("Course already exists");
				}
			}
		}
		return courseRepo.save(course);
	}

	@Override
	public Course updateCourseById(int id, Course course) throws Exception {
		Course oldCourse = selectCourseById(id);
		oldCourse.setDescription(course.getDescription());
		oldCourse.setName(course.getName());
		oldCourse.setCreditPoints(course.getCreditPoints());
		oldCourse.setStudyProgrammes(course.getStudyProgrammes());
		courseRepo.save(oldCourse);
		return oldCourse;
	}

	@Override
	public ArrayList<Course> selectByStudyProgrammeId(int id) throws Exception {
		if (courseRepo.count() == 0)
			return new ArrayList<Course>();
		return (ArrayList<Course>) courseRepo.findByStudyProgrammesStudyProgrammeId(id);
	}

	@Override
	public ArrayList<Course> selectByUserId(int id) throws Exception {
		if (courseRepo.count() == 0)
			return new ArrayList<Course>();
		return (ArrayList<Course>) courseRepo.findByUsersUserId(id);
	}

}
