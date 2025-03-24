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
import lv.venta.model.Lesson;
import lv.venta.service.ILessonService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/lesson")
public class LessonController {

	@Autowired
	private ILessonService lessonService;

	@GetMapping("/all")
	public ResponseEntity<?> getLessonList() {
		try {
			return new ResponseEntity<ArrayList<Lesson>>(lessonService.selectAllLessons(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<?> getLessonById(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<Lesson>(lessonService.selectLessonById(id), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<?> insertLesson(@RequestBody @Valid Lesson lesson) {
		try {
			return new ResponseEntity<Lesson>(lessonService.insertNewLesson(lesson), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateLessonById(@PathVariable("id") int id, @RequestBody @Valid Lesson lesson) {
		try {
			return new ResponseEntity<Lesson>(lessonService.updateLessonById(id, lesson), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteLessonById(@PathVariable("id") int id) {
		try {
			lessonService.deleteLessonById(id);
			return new ResponseEntity<ArrayList<Lesson>>(lessonService.selectAllLessons(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/studyprogramme/{name}/{year}")
	public ResponseEntity<?> getLessonByStudyProgrammeNameAndYear(@PathVariable("name") String name,
			@PathVariable("year") int year) {
		try {
			return new ResponseEntity<ArrayList<Lesson>>(lessonService.selectByStudyProgrammeNameAndYear(name, year),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/lecturer/{name}")
	public ResponseEntity<?> getLessonByLecturerName(@PathVariable("name") String name) {
		try {
			return new ResponseEntity<ArrayList<Lesson>>(lessonService.selectByLecturerFullName(name), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/classroom/{building}/{number}")
	public ResponseEntity<?> getLessonByLecturerName(@PathVariable("building") String building,
			@PathVariable("number") int number) {
		try {
			return new ResponseEntity<ArrayList<Lesson>>(
					lessonService.selectByClassroomBuildingAndNumber(building, number), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
