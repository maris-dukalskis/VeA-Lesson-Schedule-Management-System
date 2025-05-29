package lv.venta.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lv.venta.model.Classroom;
import lv.venta.service.IClassroomService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/classroom")
public class ClassroomController {

	private final IClassroomService classroomService;

	public ClassroomController(IClassroomService classroomService) {
		this.classroomService = classroomService;
	}

	@GetMapping("/all")
	public ResponseEntity<Object> getClassroomList() {
		try {
			return new ResponseEntity<>(classroomService.selectAllClassrooms(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Object> getClassroomById(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<>(classroomService.selectClassroomById(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<Object> insertClassroom(@RequestBody @Valid Classroom classroom) {
		try {
			return new ResponseEntity<>(classroomService.insertNewClassroom(classroom), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Object> updateClassroomById(@PathVariable("id") int id,
			@RequestBody @Valid Classroom classroom) {
		try {
			return new ResponseEntity<>(classroomService.updateClassroomById(id, classroom), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> deleteClassroomById(@PathVariable("id") int id) {
		try {
			classroomService.deleteClassroomById(id);
			return new ResponseEntity<>(classroomService.selectAllClassrooms(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
