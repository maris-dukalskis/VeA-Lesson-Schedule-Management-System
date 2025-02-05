package lv.venta.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
@RequestMapping("/classroom")
public class ClassroomController {

	@Autowired
	private IClassroomService classroomService;

	@GetMapping("/get/all")
	public ResponseEntity<?> getClassroomList() {
		try {
			return new ResponseEntity<ArrayList<Classroom>>(classroomService.selectAllClassrooms(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<?> getClassroomById(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<Classroom>(classroomService.selectClassroomById(id), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<?> postInsertClassroom(@RequestBody @Valid Classroom classroom, BindingResult result) {
		try {
			return new ResponseEntity<Classroom>(classroomService.insertNewClassroom(classroom), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> putClassroomUpdateById(@PathVariable("id") int id, @RequestBody @Valid Classroom classroom,
			BindingResult result) {
		try {
			return new ResponseEntity<Classroom>(classroomService.updateClassroomById(id, classroom), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteClassroomDeleteById(@PathVariable("id") int id) {
		try {
			classroomService.deleteClassroomById(id);
			return new ResponseEntity<ArrayList<Classroom>>(classroomService.selectAllClassrooms(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
