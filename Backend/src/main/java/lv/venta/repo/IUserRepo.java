package lv.venta.repo;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.User;

public interface IUserRepo extends CrudRepository<User, Integer> {

	User findByEmail(String email);

	ArrayList<User> findByCoursesCourseId(int id);

}
