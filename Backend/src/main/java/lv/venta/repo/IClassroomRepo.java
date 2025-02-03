package lv.venta.repo;

import org.springframework.data.repository.CrudRepository;

import lv.venta.model.Classroom;

public interface IClassroomRepo extends CrudRepository<Classroom, Integer>{

}
