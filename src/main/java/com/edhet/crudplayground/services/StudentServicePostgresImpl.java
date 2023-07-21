package com.edhet.crudplayground.services;

import com.edhet.crudplayground.dtos.StudentDTO;
import com.edhet.crudplayground.dtos.StudentRequest;
import com.edhet.crudplayground.exceptions.*;
import com.edhet.crudplayground.models.StudentPostgres;
import com.edhet.crudplayground.repositories.StudentRepositoryPostgres;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServicePostgresImpl implements StudentService {

    private final StudentRepositoryPostgres studentRepositoryPostgres;
    private final StudentMapper studentMapper;

    public StudentServicePostgresImpl(StudentRepositoryPostgres studentRepositoryPostgres, StudentMapper studentMapper) {
        this.studentRepositoryPostgres = studentRepositoryPostgres;
        this.studentMapper = studentMapper;
    }

    @Override
    public StudentDTO getStudent(String studentId) throws StudentNotFoundException, InvalidStudentIdFormatException {
        Long id = validPostgresId(studentId);
        return studentMapper.postgresToDto(studentRepositoryPostgres.findById(id).orElseThrow(() -> new StudentNotFoundException("No student with ID " + studentId + " in Postgres")));
    }

    @Override
    public List<StudentDTO> getStudents() {
        List<StudentDTO> studentDTOList = new ArrayList<>();
        studentRepositoryPostgres.findAll().forEach(studentPostgres -> studentDTOList.add(studentMapper.postgresToDto(studentPostgres)));
        return studentDTOList;
    }

    @Override
    public void addStudent(StudentRequest studentRequest) throws EmailTakenException {
        Optional<StudentPostgres> studentOptional = studentRepositoryPostgres.findByEmail(studentRequest.email());
        if (studentOptional.isPresent()) throw new EmailTakenException("Email taken");

        if (studentRequest.birthDate().isAfter(LocalDate.now()))
            throw new InvalidBirthDateException("The given birth date is after today");

        StudentPostgres student = studentMapper.requestToPostgres(studentRequest);
        studentRepositoryPostgres.save(student);
    }

    @Override
    public void deleteStudent(String studentId) throws StudentNotFoundException {
        Long id = validPostgresId(studentId);
        if (!studentRepositoryPostgres.existsById(id))
            throw new StudentNotFoundException("No student with ID " + studentId + " in Postgres");
        studentRepositoryPostgres.deleteById(id);
    }

    @Override
    public void updateStudent(String studentId, StudentRequest studentRequest) throws StudentNotFoundException, EmailTakenException {
        Long id = validPostgresId(studentId);
        StudentPostgres student = studentMapper.requestToPostgres(studentRequest);
        StudentPostgres studentReference = studentRepositoryPostgres.findById(id).orElseThrow(() -> new StudentNotFoundException("No student with ID " + studentId + " in Postgres"));

        if (student.getBirthDate().isAfter(LocalDate.now()))
            throw new InvalidBirthDateException("The given birth date is after today");


        if (!student.getName().isEmpty()) {
            studentReference.setName(student.getName());
        }
        if (student.getBirthDate() != null) {
            studentReference.setBirthDate(student.getBirthDate());
        }
        if (student.getGender() != null) {
            studentReference.setGender(student.getGender());
        }
        if (student.getCourse() != null) {
            studentReference.setCourse(student.getCourse());
        }
        if (!student.getEmail().isEmpty() && !student.getEmail().equals(studentReference.getEmail())) {
            if (studentRepositoryPostgres.findByEmail(student.getEmail()).isPresent())
                throw new EmailTakenException("Email taken");
            studentReference.setEmail(student.getEmail());
        }

        studentRepositoryPostgres.save(studentReference);
    }

    private Long validPostgresId(String studentId) throws InvalidStudentIdFormatException {
        long id;
        try {
            id = Long.parseLong(studentId);
        } catch (NumberFormatException e) {
            throw new InvalidStudentIdFormatException(studentId + " is not a valid Postgres ID");
        }
        return id;
    }
}
