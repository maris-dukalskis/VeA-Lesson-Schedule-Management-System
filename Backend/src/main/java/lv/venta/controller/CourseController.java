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
import lv.venta.model.Course;
import lv.venta.service.ICourseService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/course")
public class CourseController {

	private final ICourseService courseService;

	public CourseController(ICourseService courseService) {
		this.courseService = courseService;
	}

	@GetMapping("/all")
	public ResponseEntity<Object> getCourseList() {
		try {
			return new ResponseEntity<>(courseService.selectAllCourses(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Object> getCourseById(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<>(courseService.selectCourseById(id), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<Object> insertCourse(@RequestBody @Valid Course course) {
		try {
			return new ResponseEntity<>(courseService.insertNewCourse(course), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Object> updateCourseById(@PathVariable("id") int id, @RequestBody @Valid Course course) {
		try {
			return new ResponseEntity<>(courseService.updateCourseById(id, course), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> deleteCourseById(@PathVariable("id") int id) {
		try {
			courseService.deleteCourseById(id);
			return new ResponseEntity<>(courseService.selectAllCourses(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
