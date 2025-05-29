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
import lv.venta.model.Lecturer;
import lv.venta.service.ILecturerService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/lecturer")
public class LecturerController {

	private final ILecturerService lecturerService;

	public LecturerController(ILecturerService lecturerService) {
		this.lecturerService = lecturerService;
	}

	@GetMapping("/all")
	public ResponseEntity<Object> getLecturerList() {
		try {
			return new ResponseEntity<>(lecturerService.selectAllLecturers(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Object> getLecturerById(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<>(lecturerService.selectLecturerById(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<Object> insertLecturer(@RequestBody @Valid Lecturer lecturer) {
		try {
			return new ResponseEntity<>(lecturerService.insertNewLecturer(lecturer), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Object> updateLecturerById(@PathVariable("id") int id,
			@RequestBody @Valid Lecturer lecturer) {
		try {
			return new ResponseEntity<>(lecturerService.updateLecturerById(id, lecturer), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> deleteLecturerById(@PathVariable("id") int id) {
		try {
			lecturerService.deleteLecturerById(id);
			return new ResponseEntity<>(lecturerService.selectAllLecturers(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
