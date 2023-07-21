package com.edhet.crudplayground.services;

import com.edhet.crudplayground.dtos.StudentDTO;
import com.edhet.crudplayground.dtos.StudentRequest;
import com.edhet.crudplayground.exceptions.EmailTakenException;
import com.edhet.crudplayground.exceptions.InvalidBirthDateException;
import com.edhet.crudplayground.exceptions.InvalidDatabaseSelectedException;
import com.edhet.crudplayground.exceptions.StudentNotFoundException;
import com.edhet.crudplayground.repositories.StudentRepositoryMongo;
import com.edhet.crudplayground.models.StudentMongo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceMongoImpl implements StudentService {
    private final StudentRepositoryMongo studentRepositoryMongo;
    private final StudentMapper studentMapper;

    public StudentServiceMongoImpl(StudentRepositoryMongo studentRepositoryMongo, StudentMapper studentMapper) {
        this.studentRepositoryMongo = studentRepositoryMongo;
        this.studentMapper = studentMapper;
    }

    @Override
    public StudentDTO getStudent(String studentId) throws StudentNotFoundException {
        return studentMapper.mongoToDto(
                studentRepositoryMongo.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("No student with ID " + studentId + " in MongoDB"))
        );
    }

    @Override
    public List<StudentDTO> getStudents() {
        return studentRepositoryMongo.findAll().stream()
                .map(studentMapper::mongoToDto)
                .toList();
    }

    @Override
    public void addStudent(StudentRequest studentRequest) throws EmailTakenException {
        Optional<StudentMongo> studentOptional = studentRepositoryMongo.findByEmail(studentRequest.email());
        if (studentOptional.isPresent())
            throw new EmailTakenException("Email taken");

        if (studentRequest.birthDate().isAfter(LocalDate.now()))
            throw new InvalidBirthDateException("The given birth date is after today");

        StudentMongo student = studentMapper.requestToMongo(studentRequest);
        studentRepositoryMongo.save(student);
    }

    @Override
    public void deleteStudent(String studentId) throws StudentNotFoundException {
        if (!studentRepositoryMongo.existsById(studentId))
            throw new StudentNotFoundException("No student with ID " + studentId + " in MongoDB");
        studentRepositoryMongo.deleteById(studentId);
    }

    @Override
    public void updateStudent(String studentId, StudentRequest studentRequest) throws StudentNotFoundException, EmailTakenException {
        StudentMongo student = studentMapper.requestToMongo(studentRequest);
        StudentMongo studentReference = studentRepositoryMongo.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException("No student with ID " + studentId + " in MongoDB"));

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
            if (studentRepositoryMongo.findByEmail(student.getEmail()).isPresent())
                throw new EmailTakenException("Email taken");
            studentReference.setEmail(student.getEmail());
        }

        studentRepositoryMongo.save(studentReference);
    }
}
