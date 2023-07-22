package com.edhet.crudplayground.services;

import com.edhet.crudplayground.dtos.StudentDTO;
import com.edhet.crudplayground.dtos.RequestDTO;
import com.edhet.crudplayground.exceptions.EmailTakenException;
import com.edhet.crudplayground.exceptions.StudentNotFoundException;

import java.util.List;

public interface StudentService {
    StudentDTO getStudent(String studentId) throws StudentNotFoundException;
    List<StudentDTO> getStudents();
    void addStudent(RequestDTO requestDTO) throws EmailTakenException;
    void deleteStudent(String studentId) throws StudentNotFoundException;
    void updateStudent(String studentId, RequestDTO requestDTO) throws StudentNotFoundException, EmailTakenException;
}
