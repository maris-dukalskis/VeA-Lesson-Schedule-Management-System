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
import lv.venta.model.CourseStudyProgrammeAlias;
import lv.venta.service.ICourseStudyProgrammeAliasService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/coursestudyprogrammealias")
public class CourseStudyProgrammeAliasController {

	@Autowired
	private ICourseStudyProgrammeAliasService courseStudyProgrammeAliasService;

	@GetMapping("/all")
	public ResponseEntity<?> getCourseStudyProgrammeAliasList() {
		try {
			return new ResponseEntity<ArrayList<CourseStudyProgrammeAlias>>(
					courseStudyProgrammeAliasService.selectAllCourseStudyProgrammeAliases(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<?> getCourseStudyProgrammeAliasById(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<CourseStudyProgrammeAlias>(
					courseStudyProgrammeAliasService.selectCourseStudyProgrammeAliasById(id), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<?> insertCourseStudyProgrammeAlias(
			@RequestBody @Valid CourseStudyProgrammeAlias courseStudyProgrammeAlias) {
		try {
			return new ResponseEntity<CourseStudyProgrammeAlias>(
					courseStudyProgrammeAliasService.insertNewCourseStudyProgrammeAlias(courseStudyProgrammeAlias),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateCourseStudyProgrammeAliasById(@PathVariable("id") int id,
			@RequestBody @Valid CourseStudyProgrammeAlias courseStudyProgrammeAlias) {
		try {
			return new ResponseEntity<CourseStudyProgrammeAlias>(
					courseStudyProgrammeAliasService.updateCourseStudyProgrammeAliasById(id, courseStudyProgrammeAlias),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteCourseStudyProgrammeAliasById(@PathVariable("id") int id) {
		try {
			courseStudyProgrammeAliasService.deleteCourseStudyProgrammeAliasById(id);
			return new ResponseEntity<ArrayList<CourseStudyProgrammeAlias>>(
					courseStudyProgrammeAliasService.selectAllCourseStudyProgrammeAliases(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
