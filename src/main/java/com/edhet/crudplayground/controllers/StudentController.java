package com.edhet.crudplayground.controllers;

import com.edhet.crudplayground.dtos.StudentDTO;
import com.edhet.crudplayground.dtos.RequestDTO;
import com.edhet.crudplayground.services.StudentService;
import com.edhet.crudplayground.services.StudentServiceFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"*"}, allowedHeaders = {"*"})
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
    public void addStudent(@RequestParam String database, @RequestBody RequestDTO requestDTO) {
        StudentService studentService = studentServiceFactory.getStudentService(database);
        studentService.addStudent(requestDTO);
    }

    @PutMapping(path = "{studentId}")
    public void updateStudentById(@RequestParam String database, @PathVariable("studentId") String studentId, @RequestBody RequestDTO requestDTO) {
        StudentService studentService = studentServiceFactory.getStudentService(database);
        studentService.updateStudent(studentId, requestDTO);
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudentById(@RequestParam String database, @PathVariable("studentId") String studentId) {
        StudentService studentService = studentServiceFactory.getStudentService(database);
        studentService.deleteStudent(studentId);
    }
}
