package lv.venta.repo;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.User;

public interface IUserRepo extends CrudRepository<User, Integer> {

}
