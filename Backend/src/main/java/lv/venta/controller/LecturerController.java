package lv.venta.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
import lv.venta.model.Lecturer;
import lv.venta.service.ILecturerService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/lecturer")
public class LecturerController {

	@Autowired
	private ILecturerService lecturerService;

	@GetMapping("/get/all")
	public ResponseEntity<?> getLecturerList() {
		try {
			return new ResponseEntity<ArrayList<Lecturer>>(lecturerService.selectAllLecturers(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<?> getLecturerById(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<Lecturer>(lecturerService.selectLecturerById(id), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<?> postInsertLecturer(@RequestBody @Valid Lecturer lecturer, BindingResult result) {
		try {
			return new ResponseEntity<Lecturer>(lecturerService.insertNewLecturer(lecturer), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> putLecturerUpdateById(@PathVariable("id") int id, @RequestBody @Valid Lecturer lecturer,
			BindingResult result) {
		try {
			return new ResponseEntity<Lecturer>(lecturerService.updateLecturerById(id, lecturer), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteLecturerDeleteById(@PathVariable("id") int id) {
		try {
			lecturerService.deleteLecturerById(id);
			return new ResponseEntity<ArrayList<Lecturer>>(lecturerService.selectAllLecturers(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
