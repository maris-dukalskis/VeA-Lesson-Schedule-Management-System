package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lv.venta.model.Lesson;
import lv.venta.model.User;
import lv.venta.repo.ILessonRepo;
import lv.venta.repo.IUserRepo;
import lv.venta.service.ILessonService;
import lv.venta.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	private final IUserRepo userRepo;

	private final ILessonService lessonService;

	private final ILessonRepo lessonRepo;

	public UserServiceImpl(IUserRepo userRepo, ILessonService lessonService, ILessonRepo lessonRepo) {
		this.userRepo = userRepo;
		this.lessonService = lessonService;
		this.lessonRepo = lessonRepo;
	}

	@Override
	public ArrayList<User> selectAllUsers() throws Exception {
		if (userRepo.count() == 0)
			return new ArrayList<User>();
		return (ArrayList<User>) userRepo.findAll();
	}

	@Override
	public User selectUserById(int id) throws Exception {
		if (id < 0)
			throw new Exception("ID cannot be below 0");
		if (!userRepo.existsById(id)) {
			throw new Exception("User by that ID does not exist");
		}
		return userRepo.findById(id).get();
	}

	@Override
	public void deleteUserById(int id) throws Exception {
		ArrayList<Lesson> lessons = lessonService.selectByUserId(id);
		if (!lessons.isEmpty()) {
			for (Lesson lesson : lessons) {
				lesson.setLecturer(null);
			}
			lessonRepo.saveAll(lessons);
		}
		userRepo.delete(selectUserById(id));
	}

	@Override
	public User insertNewUser(User user) throws Exception {
		if (user == null)
			throw new Exception("User object cannot be null");
		List<User> users = new ArrayList<>();
		try {
			users = selectAllUsers();
		} catch (Exception e) {
		}
		if (!users.isEmpty()) {
			for (User dbUser : users) {
				if (dbUser.getFullName().equals(user.getFullName()) && dbUser.getEmail().equals(user.getEmail())) {
					throw new Exception("user already exists");
				}
			}
		}
		return userRepo.save(user);
	}

	@Override
	public User updateUserById(int id, User user) throws Exception {
		User oldUser = selectUserById(id);
		oldUser.setEmail(user.getEmail());
		oldUser.setFullName(user.getFullName());
		oldUser.setRole(user.getRole());
		userRepo.save(oldUser);
		return oldUser;
	}

	@Override
	public User selectByEmail(String email) throws Exception {
		if (userRepo.count() == 0)
			throw new Exception("User by that email does not exist");
		return userRepo.findByEmail(email);
	}

	@Override
	public ArrayList<User> selectByCourseId(int id) throws Exception {
		if (userRepo.count() == 0)
			return new ArrayList<User>();
		return (ArrayList<User>) userRepo.findByCoursesCourseId(id);
	}

}
