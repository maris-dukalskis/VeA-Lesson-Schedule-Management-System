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
import lv.venta.model.StudyProgramme;
import lv.venta.service.IStudyProgrammeService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/studyprogramme")
public class StudyProgrammeController {

	@Autowired
	private IStudyProgrammeService studyProgrammeService;

	@GetMapping("/all")
	public ResponseEntity<?> getStudyProgrammeList() {
		try {
			return new ResponseEntity<ArrayList<StudyProgramme>>(studyProgrammeService.selectAllStudyProgrammes(),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<?> getStudyProgrammeById(@PathVariable("id") int id) {
		try {
			return new ResponseEntity<StudyProgramme>(studyProgrammeService.selectStudyProgrammeById(id),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<?> postInsertStudyProgramme(@RequestBody @Valid StudyProgramme studyProgramme,
			BindingResult result) {
		try {
			return new ResponseEntity<StudyProgramme>(studyProgrammeService.insertNewStudyProgramme(studyProgramme),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> putStudyProgrammeUpdateById(@PathVariable("id") int id,
			@RequestBody @Valid StudyProgramme studyProgramme, BindingResult result) {
		try {
			return new ResponseEntity<StudyProgramme>(
					studyProgrammeService.updateStudyProgrammeById(id, studyProgramme), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteStudyProgrammeDeleteById(@PathVariable("id") int id) {
		try {
			studyProgrammeService.deleteStudyProgrammeById(id);
			return new ResponseEntity<ArrayList<StudyProgramme>>(studyProgrammeService.selectAllStudyProgrammes(),
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
