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
import lv.venta.model.CancelledLessonDateTime;
import lv.venta.service.ICancelledLessonDateTimeService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/cancelledlessondatetime")
public class CancelledLessonDateTimeController {

	private final ICancelledLessonDateTimeService cancelledLessonDateTimeService;

	public CancelledLessonDateTimeController(ICancelledLessonDateTimeService cancelledLessonDateTimeService) {
		this.cancelledLessonDateTimeService = cancelledLessonDateTimeService;
	}

	@GetMapping("/all")
	public ResponseEntity<Object> getCancelledLessonDateTimeList() {
		try {
			return new ResponseEntity<>(cancelledLessonDateTimeService.selectAllCancelledLessonDateTimes(),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Object> getCancelledLessonDateTimeById(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<>(cancelledLessonDateTimeService.selectCancelledLessonDateTimeById(id),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<Object> insertCancelledLessonDateTime(
			@RequestBody @Valid CancelledLessonDateTime cancelledLessonDateTime) {
		try {
			return new ResponseEntity<>(
					cancelledLessonDateTimeService.insertNewCancelledLessonDateTime(cancelledLessonDateTime),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Object> updateCancelledLessonDateTimeById(@PathVariable("id") int id,
			@RequestBody @Valid CancelledLessonDateTime cancelledLessonDateTime) {
		try {
			return new ResponseEntity<>(
					cancelledLessonDateTimeService.updateCancelledLessonDateTimeById(id, cancelledLessonDateTime),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> deleteCancelledLessonDateTimeById(@PathVariable("id") int id) {
		try {
			cancelledLessonDateTimeService.deleteCancelledLessonDateTimeById(id);
			return new ResponseEntity<>(cancelledLessonDateTimeService.selectAllCancelledLessonDateTimes(),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
