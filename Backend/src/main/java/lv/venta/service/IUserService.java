package lv.venta.service;

import java.util.List;
import java.util.NoSuchElementException;

import lv.venta.model.User;

public interface IUserService {

	public abstract List<User> selectAllUsers();

	public abstract User selectUserById(int id) throws IllegalArgumentException, NoSuchElementException;

	public abstract void deleteUserById(int id);

	public abstract User insertNewUser(User user) throws NullPointerException, IllegalStateException;

	public abstract User updateUserById(int id, User user);

	public abstract User selectByEmail(String email) throws IllegalArgumentException;

	public abstract List<User> selectByCourseId(int id);
}
