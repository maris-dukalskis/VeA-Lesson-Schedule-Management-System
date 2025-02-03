package lv.venta.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lv.venta.model.Student;
import lv.venta.repo.IStudentRepo;
import lv.venta.service.IStudentService;

@Service
public class StudentServiceImpl implements IStudentService {

	@Autowired
	IStudentRepo studentRepo;

	@Override
	public ArrayList<Student> selectAllStudents() throws Exception {
		if (studentRepo.count() == 0)
			throw new Exception("Student table is empty");
		return (ArrayList<Student>) studentRepo.findAll();
	}

	@Override
	public Student selectStudentById(int id) throws Exception {
		if (id < 0)
			throw new Exception("ID cannot be below 0");
		if (!studentRepo.existsById(id)) {
			throw new Exception("Student by that ID does not exist");
		}
		return studentRepo.findById(id).get();
	}

	@Override
	public void deleteStudentById(int id) throws Exception {
		studentRepo.delete(selectStudentById(id));
	}

	@Override
	public Student insertNewStudent(Student student) throws Exception {
		if (student == null)
			throw new Exception("Student object cannot be null");
		List<Student> students = selectAllStudents();
		if (!students.isEmpty()) {
			for (Student dbStudent : students) {
				if (dbStudent.getMatriculeNumber() == student.getMatriculeNumber()) {
					throw new Exception("Student already exists");
				}
			}
		}
		return studentRepo.save(student);
	}

	@Override
	public Student updateStudentById(int id, Student student) throws Exception {
		Student oldStudent = selectStudentById(id);
		oldStudent.setEmail(student.getEmail());
		oldStudent.setFullName(student.getFullName());
		oldStudent.setStudyProgramme(student.getStudyProgramme());
		oldStudent.setMatriculeNumber(student.getMatriculeNumber());
		studentRepo.save(oldStudent);
		return oldStudent;
	}

}
