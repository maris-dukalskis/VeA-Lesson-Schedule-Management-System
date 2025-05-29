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
import lv.venta.model.LessonDateTime;
import lv.venta.service.ILessonDateTimeService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/lessondatetime")
public class LessonDateTimeController {

	private final ILessonDateTimeService lessonDateTimeService;

	public LessonDateTimeController(ILessonDateTimeService lessonDateTimeService) {
		this.lessonDateTimeService = lessonDateTimeService;
	}

	@GetMapping("/all")
	public ResponseEntity<Object> getLessonDateTimeList() {
		try {
			return new ResponseEntity<>(lessonDateTimeService.selectAllLessonDateTimes(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Object> getLessonDateTimeById(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<>(lessonDateTimeService.selectLessonDateTimeById(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<Object> insertLessonDateTime(@RequestBody @Valid LessonDateTime lessonDateTime) {
		try {
			return new ResponseEntity<>(lessonDateTimeService.insertNewLessonDateTime(lessonDateTime), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Object> updateLessonDateTimeById(@PathVariable("id") int id,
			@RequestBody @Valid LessonDateTime lessonDateTime) {
		try {
			return new ResponseEntity<>(lessonDateTimeService.updateLessonDateTimeById(id, lessonDateTime),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> deleteLessonDateTimeById(@PathVariable("id") int id) {
		try {
			lessonDateTimeService.deleteLessonDateTimeById(id);
			return new ResponseEntity<>(lessonDateTimeService.selectAllLessonDateTimes(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/lesson/{id}")
	public ResponseEntity<Object> getLessonDateTimeListByLessonId(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<>(lessonDateTimeService.selectAllByLessonLessonId(id), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
