package lv.venta.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
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
import lv.venta.model.Semester;
import lv.venta.service.ISemesterService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/semester")
public class SemesterController {

	@Autowired
	private ISemesterService semesterService;

	@GetMapping("/all")
	public ResponseEntity<?> getSemesterList() {
		try {
			return new ResponseEntity<ArrayList<Semester>>(semesterService.selectAllSemesters(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<?> getSemesterById(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<Semester>(semesterService.selectSemesterById(id), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<?> insertSemester(@RequestBody @Valid Semester semester) {
		try {
			return new ResponseEntity<Semester>(semesterService.insertNewSemester(semester), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateSemesterById(@PathVariable("id") int id, @RequestBody @Valid Semester semester) {
		try {
			return new ResponseEntity<Semester>(semesterService.updateSemesterById(id, semester), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteSemesterById(@PathVariable("id") int id) {
		try {
			semesterService.deleteSemesterById(id);
			return new ResponseEntity<ArrayList<Semester>>(semesterService.selectAllSemesters(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
