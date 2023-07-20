package com.edhet.crudplayground.services;

import com.edhet.crudplayground.dtos.StudentRequest;
import com.edhet.crudplayground.exceptions.EmailTakenException;
import com.edhet.crudplayground.exceptions.StudentNotFoundException;
import com.edhet.crudplayground.models.StudentMongo;
import com.edhet.crudplayground.repositories.StudentRepositoryMongo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static com.edhet.crudplayground.models.Gender.FEMALE;
import static com.edhet.crudplayground.models.Gender.MALE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceMongoImplTest {

    @Mock
    private StudentMapper studentMapper;
    @Mock
    private StudentRepositoryMongo studentRepositoryMongo;
    private StudentServiceMongoImpl studentServiceMongo;


    @BeforeEach
    void setUp() {
        studentServiceMongo = new StudentServiceMongoImpl(studentRepositoryMongo, studentMapper);
    }

    @Test
    void getStudent_Success() {
        // GIVEN
        StudentMongo expected = new StudentMongo("name", "email@email.com", "course", LocalDate.of(2000, 1, 1), MALE);
        String id = "id";

        when(studentRepositoryMongo.findById(id)).thenReturn(Optional.of(expected));

        // WHEN
        studentServiceMongo.getStudent(id);

        // THEN
        verify(studentRepositoryMongo).findById(id);
        verify(studentMapper).mongoToDto(expected);
    }

    @Test
    void getStudent_StudentNotFound() {
        // GIVEN
        String id = "id";
        when(studentRepositoryMongo.findById(id)).thenReturn(Optional.empty());

        // THEN
        assertThrows(StudentNotFoundException.class, () -> studentServiceMongo.getStudent(id));
        verify(studentMapper, never()).mongoToDto(any());
    }

    @Test
    void getStudents_Success() {
        // WHEN
        studentServiceMongo.getStudents();

        // THEN
        verify(studentRepositoryMongo).findAll();
    }

    @Test
    void addStudent_Success() {
        // GIVEN
        StudentRequest request = new StudentRequest("name", "email@email.com", "course", LocalDate.of(2000, 1, 1), MALE);
        StudentMongo expected = new StudentMongo("name", "email@email.com", "course", LocalDate.of(2000, 1, 1), MALE);

        when(studentMapper.requestToMongo(request)).thenReturn(expected);

        // WHEN
        studentServiceMongo.addStudent(request);

        // THEN
        verify(studentMapper).requestToMongo(request);
        verify(studentRepositoryMongo).save(expected);
    }

    @Test
    void addStudent_EmailTaken() {
        // GIVEN
        StudentRequest student = new StudentRequest("name", "email@email.com", "course", LocalDate.of(2000, 1, 1), MALE);
        StudentMongo expected = new StudentMongo("name", "email@email.com", "course", LocalDate.of(2000, 1, 1), MALE);

        when(studentRepositoryMongo.findByEmail(student.email())).thenReturn(Optional.of(expected));

        // THEN
        assertThrows(EmailTakenException.class, () -> studentServiceMongo.addStudent(student));
        verify(studentMapper, never()).requestToMongo(any());
        verify(studentRepositoryMongo, never()).save(any());
    }

    @Test
    void deleteStudent_Success() {
        // GIVEN
        String id = "id";
        when(studentRepositoryMongo.existsById(id)).thenReturn(true);

        //WHEN
        studentServiceMongo.deleteStudent(id);

        // THEN
        verify(studentRepositoryMongo).deleteById(id);
    }

    @Test
    void deleteStudent_StudentNotFound() {
        // GIVEN
        String id = "id";
        when(studentRepositoryMongo.existsById(id)).thenReturn(false);

        // THEN
        assertThrows(StudentNotFoundException.class, () -> studentServiceMongo.deleteStudent(id));
        verify(studentRepositoryMongo, never()).deleteById(any());
    }

    @Test
    void updateStudent_Success() {
        // GIVEN
        StudentRequest request = new StudentRequest("new", "new@new.com", "new", LocalDate.of(2020, 1, 1), FEMALE);
        StudentMongo student = new StudentMongo("name", "email@email.com", "course", LocalDate.of(2000, 1, 1), MALE);

        StudentMongo expected = new StudentMongo("new", "new@email.com", "new", LocalDate.of(2020, 1, 1), FEMALE);

        String id = "id";

        when(studentMapper.requestToMongo(request)).thenReturn(expected);
        when(studentRepositoryMongo.findById(id)).thenReturn(Optional.of(student));

        // WHEN
        studentServiceMongo.updateStudent(id, request);

        // THEN
        verify(studentMapper).requestToMongo(request);
        verify(studentRepositoryMongo).findById(id);
        // should this even work 🤨
        assertEquals(expected, student);
        verify(studentRepositoryMongo).save(student);
    }

    @Test
    void updateStudent_StudentNotFound() {
        //GIVEN
        StudentRequest request = new StudentRequest("new", "new@new.com", "new", LocalDate.of(2020, 1, 1), FEMALE);
        String id = "id";
        when(studentRepositoryMongo.findById(id)).thenReturn(Optional.empty());

        //THEN
        assertThrows(StudentNotFoundException.class, () -> studentServiceMongo.updateStudent(id, request));
        verify(studentRepositoryMongo, never()).save(any());
    }

    @Test
    void updateStudent_EmailTaken() {
        // GIVEN
        StudentRequest request = new StudentRequest("new", "new@new.com", "new", LocalDate.of(2020, 1, 1), FEMALE);
        StudentMongo anyNonNullMongo = new StudentMongo("new", "new@email.com", "new", LocalDate.of(2020, 1, 1), FEMALE);

        String id = "id";

        when(studentMapper.requestToMongo(request)).thenReturn(anyNonNullMongo);
        when(studentRepositoryMongo.findById(id)).thenReturn(Optional.of(anyNonNullMongo));
        when(studentRepositoryMongo.findByEmail(anyNonNullMongo.getEmail())).thenReturn(Optional.of(anyNonNullMongo));

        // THEN
        assertThrows(EmailTakenException.class, () -> studentServiceMongo.updateStudent(id, request));
        verify(studentRepositoryMongo, never()).save(any());
    }

}