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
import lv.venta.model.CourseStudyProgrammeAlias;
import lv.venta.service.ICourseStudyProgrammeAliasService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/coursestudyprogrammealias")
public class CourseStudyProgrammeAliasController {

	private final ICourseStudyProgrammeAliasService courseStudyProgrammeAliasService;

	public CourseStudyProgrammeAliasController(ICourseStudyProgrammeAliasService courseStudyProgrammeAliasService) {
		this.courseStudyProgrammeAliasService = courseStudyProgrammeAliasService;
	}

	@GetMapping("/all")
	public ResponseEntity<Object> getCourseStudyProgrammeAliasList() {
		try {
			return new ResponseEntity<>(courseStudyProgrammeAliasService.selectAllCourseStudyProgrammeAliases(),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Object> getCourseStudyProgrammeAliasById(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<>(courseStudyProgrammeAliasService.selectCourseStudyProgrammeAliasById(id),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<Object> insertCourseStudyProgrammeAlias(
			@RequestBody @Valid CourseStudyProgrammeAlias courseStudyProgrammeAlias) {
		try {
			return new ResponseEntity<>(
					courseStudyProgrammeAliasService.insertNewCourseStudyProgrammeAlias(courseStudyProgrammeAlias),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Object> updateCourseStudyProgrammeAliasById(@PathVariable("id") int id,
			@RequestBody @Valid CourseStudyProgrammeAlias courseStudyProgrammeAlias) {
		try {
			return new ResponseEntity<>(
					courseStudyProgrammeAliasService.updateCourseStudyProgrammeAliasById(id, courseStudyProgrammeAlias),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> deleteCourseStudyProgrammeAliasById(@PathVariable("id") int id) {
		try {
			courseStudyProgrammeAliasService.deleteCourseStudyProgrammeAliasById(id);
			return new ResponseEntity<>(courseStudyProgrammeAliasService.selectAllCourseStudyProgrammeAliases(),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/course/{id}")
	public ResponseEntity<Object> getCourseStudyProgrammeAliasesByCourseId(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<>(courseStudyProgrammeAliasService.selectByCourseId(id), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
