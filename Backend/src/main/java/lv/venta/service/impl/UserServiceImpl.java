package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.User;
import lv.venta.repo.IUserRepo;
import lv.venta.service.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	IUserRepo userRepo;

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

}
