package lv.venta.repo;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Lecturer;

public interface ILecturerRepo extends CrudRepository<Lecturer, Integer> {

}
