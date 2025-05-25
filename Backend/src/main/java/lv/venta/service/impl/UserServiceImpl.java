package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
	public List<User> selectAllUsers() {
		if (userRepo.count() == 0)
			return new ArrayList<>();
		return (List<User>) userRepo.findAll();
	}

	@Override
	public User selectUserById(int id) throws IllegalArgumentException, NoSuchElementException {
		if (id < 0)
			throw new IllegalArgumentException("ID cannot be below 0");
		if (!userRepo.existsById(id)) {
			throw new NoSuchElementException("User by that ID does not exist");
		}
		return userRepo.findById(id).get();
	}

	@Override
	public void deleteUserById(int id) {
		List<Lesson> lessons = lessonService.selectByUserId(id);
		if (!lessons.isEmpty()) {
			for (Lesson lesson : lessons) {
				lesson.setLecturer(null);
			}
			lessonRepo.saveAll(lessons);
		}
		userRepo.delete(selectUserById(id));
	}

	@Override
	public User insertNewUser(User user) throws NullPointerException, IllegalStateException {
		if (user == null)
			throw new NullPointerException("User object cannot be null");

		List<User> users = selectAllUsers();

		if (!users.isEmpty()) {
			for (User dbUser : users) {
				if (dbUser.getFullName().equals(user.getFullName()) && dbUser.getEmail().equals(user.getEmail())) {
					throw new IllegalStateException("User already exists");
				}
			}
		}
		return userRepo.save(user);
	}

	@Override
	public User updateUserById(int id, User user) {
		User oldUser = selectUserById(id);
		oldUser.setEmail(user.getEmail());
		oldUser.setFullName(user.getFullName());
		oldUser.setRole(user.getRole());
		userRepo.save(oldUser);
		return oldUser;
	}

	@Override
	public User selectByEmail(String email) throws IllegalArgumentException {
		if (userRepo.count() == 0)
			throw new IllegalArgumentException("User by that email does not exist");
		return userRepo.findByEmail(email);
	}

	@Override
	public List<User> selectByCourseId(int id) {
		if (userRepo.count() == 0)
			return new ArrayList<>();
		return userRepo.findByCoursesCourseId(id);
	}

}
