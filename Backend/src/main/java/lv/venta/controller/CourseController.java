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
import lv.venta.model.Course;
import lv.venta.service.ICourseService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/course")
public class CourseController {

	@Autowired
	private ICourseService courseService;

	@GetMapping("/all")
	public ResponseEntity<?> getCourseList() {
		try {
			return new ResponseEntity<ArrayList<Course>>(courseService.selectAllCourses(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<?> getCourseById(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<Course>(courseService.selectCourseById(id), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<?> postInsertCourse(@RequestBody @Valid Course course, BindingResult result) {
		try {
			return new ResponseEntity<Course>(courseService.insertNewCourse(course), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> putCourseUpdateById(@PathVariable("id") int id, @RequestBody @Valid Course course,
			BindingResult result) {
		try {
			return new ResponseEntity<Course>(courseService.updateCourseById(id, course), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteCourseDeleteById(@PathVariable("id") int id) {
		try {
			courseService.deleteCourseById(id);
			return new ResponseEntity<ArrayList<Course>>(courseService.selectAllCourses(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
