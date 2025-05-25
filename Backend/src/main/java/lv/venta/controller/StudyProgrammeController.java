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
import lv.venta.model.StudyProgramme;
import lv.venta.service.IStudyProgrammeService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/studyprogramme")
public class StudyProgrammeController {

	private final IStudyProgrammeService studyProgrammeService;

	public StudyProgrammeController(IStudyProgrammeService studyProgrammeService) {
		this.studyProgrammeService = studyProgrammeService;
	}

	@GetMapping("/all")
	public ResponseEntity<Object> getStudyProgrammeList() {
		try {
			return new ResponseEntity<>(studyProgrammeService.selectAllStudyProgrammes(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<Object> getStudyProgrammeById(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<>(studyProgrammeService.selectStudyProgrammeById(id), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<Object> insertStudyProgramme(@RequestBody @Valid StudyProgramme studyProgramme) {
		try {
			return new ResponseEntity<>(studyProgrammeService.insertNewStudyProgramme(studyProgramme), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Object> updateStudyProgrammeById(@PathVariable("id") int id,
			@RequestBody @Valid StudyProgramme studyProgramme) {
		try {
			return new ResponseEntity<>(studyProgrammeService.updateStudyProgrammeById(id, studyProgramme),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Object> deleteStudyProgrammeById(@PathVariable("id") int id) {
		try {
			studyProgrammeService.deleteStudyProgrammeById(id);
			return new ResponseEntity<>(studyProgrammeService.selectAllStudyProgrammes(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
