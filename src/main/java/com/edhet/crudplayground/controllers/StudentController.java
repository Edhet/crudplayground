package com.edhet.crudplayground.controllers;

import com.edhet.crudplayground.dtos.StudentDTO;
import com.edhet.crudplayground.dtos.StudentRequest;
import com.edhet.crudplayground.services.StudentService;
import com.edhet.crudplayground.services.StudentServiceFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/student")
public class StudentController {

    private final StudentServiceFactory studentServiceFactory;

    public StudentController(StudentServiceFactory studentServiceFactory) {
        this.studentServiceFactory = studentServiceFactory;
    }

    @GetMapping
    public List<StudentDTO> getStudents(@RequestParam String database) {
        StudentService studentService = studentServiceFactory.getStudentService(database);
        return studentService.getStudents();
    }

    @GetMapping(path = "{studentId}")
    public StudentDTO getStudentById(@RequestParam String database, @PathVariable("studentId") String studentId) {
        StudentService studentService = studentServiceFactory.getStudentService(database);
        return studentService.getStudent(studentId);
    }

    @PostMapping
    public void addStudent(@RequestParam String database, @RequestBody StudentRequest studentRequest) {
        StudentService studentService = studentServiceFactory.getStudentService(database);
        studentService.addStudent(studentRequest);
    }

    @PutMapping(path = "{studentId}")
    public void updateStudentById(@RequestParam String database, @PathVariable("studentId") String studentId, @RequestBody StudentRequest studentRequest) {
        StudentService studentService = studentServiceFactory.getStudentService(database);
        studentService.updateStudent(studentId, studentRequest);
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudentById(@RequestParam String database, @PathVariable("studentId") String studentId) {
        StudentService studentService = studentServiceFactory.getStudentService(database);
        studentService.deleteStudent(studentId);
    }
}
