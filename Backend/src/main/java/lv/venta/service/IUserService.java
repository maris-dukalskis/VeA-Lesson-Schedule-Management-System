package lv.venta.service;

import java.util.ArrayList;

import lv.venta.model.User;

public interface IUserService {

	public abstract ArrayList<User> selectAllUsers() throws Exception;

	public abstract User selectUserById(int id) throws Exception;

	public abstract void deleteUserById(int id) throws Exception;

	public abstract User insertNewUser(User user) throws Exception;

	public abstract User updateUserById(int id, User user) throws Exception;

	public abstract User selectByEmail(String email) throws Exception;

	public abstract ArrayList<User> selectByCourseId(int id) throws Exception;
}
