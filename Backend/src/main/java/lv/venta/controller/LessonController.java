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
import lv.venta.model.Lesson;
import lv.venta.service.ILessonService;

@RestController
@RequestMapping("/lesson")
public class LessonController {

	@Autowired
	private ILessonService lessonService;

	@GetMapping("/get/all")
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
	public ResponseEntity<?> postInsertLesson(@RequestBody @Valid Lesson lesson, BindingResult result) {
		try {
			return new ResponseEntity<Lesson>(lessonService.insertNewLesson(lesson), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> putLessonUpdateById(@PathVariable("id") int id, @RequestBody @Valid Lesson lesson,
			BindingResult result) {
		try {
			return new ResponseEntity<Lesson>(lessonService.updateLessonById(id, lesson), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteLessonDeleteById(@PathVariable("id") int id) {
		try {
			lessonService.deleteLessonById(id);
			return new ResponseEntity<ArrayList<Lesson>>(lessonService.selectAllLessons(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
