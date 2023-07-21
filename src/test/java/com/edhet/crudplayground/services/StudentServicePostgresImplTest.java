package com.edhet.crudplayground.services;

import com.edhet.crudplayground.dtos.StudentRequest;
import com.edhet.crudplayground.exceptions.EmailTakenException;
import com.edhet.crudplayground.exceptions.InvalidDatabaseSelectedException;
import com.edhet.crudplayground.exceptions.InvalidStudentIdFormatException;
import com.edhet.crudplayground.exceptions.StudentNotFoundException;
import com.edhet.crudplayground.models.StudentMongo;
import com.edhet.crudplayground.models.StudentPostgres;
import com.edhet.crudplayground.repositories.StudentRepositoryMongo;
import com.edhet.crudplayground.repositories.StudentRepositoryPostgres;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Optional;

import static com.edhet.crudplayground.models.Gender.FEMALE;
import static com.edhet.crudplayground.models.Gender.MALE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServicePostgresImplTest {
    private final LocalDate NEW_BIRTH_DATE = LocalDate.of(2020, 1, 1);
    private final LocalDate BIRTH_DATE = LocalDate.of(2000, 1, 1);


    @Mock
    private StudentMapper studentMapper;
    @Mock
    private StudentRepositoryPostgres studentRepositoryPostgres;

    private StudentServicePostgresImpl studentServicePostgres;


    @BeforeEach
    void setUp() {
        studentServicePostgres = new StudentServicePostgresImpl(studentRepositoryPostgres, studentMapper);
    }

    @Test
    void getStudent_Success() {
        // GIVEN
        StudentPostgres expected = new StudentPostgres("name", "email@email.com", "course", BIRTH_DATE, MALE);
        String stringId = "1";
        long expectedId = 1L;

        when(studentRepositoryPostgres.findById(expectedId)).thenReturn(Optional.of(expected));

        // WHEN
        studentServicePostgres.getStudent(stringId);

        // THEN
        verify(studentRepositoryPostgres).findById(expectedId);
        verify(studentMapper).postgresToDto(expected);
    }

    @Test
    void getStudent_StudentNotFound() {
        // GIVEN
        String stringId = "1";
        when(studentRepositoryPostgres.findById(anyLong())).thenReturn(Optional.empty());

        // THEN
        assertThrows(StudentNotFoundException.class, () -> studentServicePostgres.getStudent(stringId));
        verify(studentMapper, never()).mongoToDto(any());
    }

    @Test
    void getStudent_InvalidStudentIdFormat() {
        // GIVEN
        String invalidStringId = "a";

        // THEN
        assertThrows(InvalidStudentIdFormatException.class, () -> studentServicePostgres.getStudent(invalidStringId));
        verify(studentRepositoryPostgres, never()).findById(any());
        verify(studentMapper, never()).mongoToDto(any());
    }

    @Test
    void getStudents_Success() {
        // WHEN
        studentServicePostgres.getStudents();

        // THEN
        verify(studentRepositoryPostgres).findAll();
    }

    @Test
    void addStudent_Success() {
        // GIVEN
        StudentRequest request = new StudentRequest("name", "email@email.com", "course", BIRTH_DATE, MALE);
        StudentPostgres expected = new StudentPostgres("name", "email@email.com", "course", BIRTH_DATE, MALE);

        when(studentMapper.requestToPostgres(request)).thenReturn(expected);

        // WHEN
        studentServicePostgres.addStudent(request);

        // THEN
        verify(studentMapper).requestToPostgres(request);
        verify(studentRepositoryPostgres).save(expected);
    }


    @Test
    void addStudent_EmailTaken() {
        // GIVEN
        StudentRequest student = new StudentRequest("name", "email@email.com", "course", BIRTH_DATE, MALE);
        StudentPostgres expected = new StudentPostgres("name", "email@email.com", "course", BIRTH_DATE, MALE);

        when(studentRepositoryPostgres.findByEmail(student.email())).thenReturn(Optional.of(expected));

        // THEN
        assertThrows(EmailTakenException.class, () -> studentServicePostgres.addStudent(student));
        verify(studentMapper, never()).requestToPostgres(any());
        verify(studentRepositoryPostgres, never()).save(any());
    }

    @Test
    void deleteStudent_Success() {
        // GIVEN
        String stringId = "1";
        long expectedId = 1L;
        when(studentRepositoryPostgres.existsById(expectedId)).thenReturn(true);

        //WHEN
        studentServicePostgres.deleteStudent(stringId);

        // THEN
        verify(studentRepositoryPostgres).deleteById(expectedId);
    }

    @Test
    void deleteStudent_StudentNotFound() {
        // GIVEN
        String id = "1";
        long expectedId = 1L;
        when(studentRepositoryPostgres.existsById(expectedId)).thenReturn(false);

        // THEN
        assertThrows(StudentNotFoundException.class, () -> studentServicePostgres.deleteStudent(id));
        verify(studentRepositoryPostgres, never()).deleteById(any());
    }

    @Test
    void deleteStudent_InvalidStudentIdFormat() {
        // GIVEN
        String invalidId = "a";

        // THEN
        assertThrows(InvalidStudentIdFormatException.class, () -> studentServicePostgres.deleteStudent(invalidId));
        verify(studentRepositoryPostgres, never()).deleteById(any());
    }

    @Test
    void updateStudent_Success() {
        // GIVEN
        StudentRequest request = new StudentRequest("new", "new@new.com", "new", NEW_BIRTH_DATE, FEMALE);
        StudentPostgres student = new StudentPostgres("name", "email@email.com", "course", BIRTH_DATE, MALE);

        StudentPostgres expected = new StudentPostgres("new", "new@email.com", "new", NEW_BIRTH_DATE, FEMALE);

        String stringId = "1";
        long expectedId = 1L;
        when(studentMapper.requestToPostgres(request)).thenReturn(expected);
        when(studentRepositoryPostgres.findById(expectedId)).thenReturn(Optional.of(student));

        // WHEN
        studentServicePostgres.updateStudent(stringId, request);

        // THEN
        verify(studentMapper).requestToPostgres(request);
        verify(studentRepositoryPostgres).findById(expectedId);
        assertEquals(expected, student);
        verify(studentRepositoryPostgres).save(student);
    }

    @Test
    void updateStudent_StudentNotFound() {
        //GIVEN
        StudentRequest request = new StudentRequest("new", "new@new.com", "new", NEW_BIRTH_DATE, FEMALE);
        String stringId = "1";
        long expectedId = 1L;
        when(studentRepositoryPostgres.findById(expectedId)).thenReturn(Optional.empty());

        //THEN
        assertThrows(StudentNotFoundException.class, () -> studentServicePostgres.updateStudent(stringId, request));
        verify(studentRepositoryPostgres, never()).save(any());
    }


    @Test
    void updateStudent_EmailTaken() {
        // GIVEN
        StudentRequest request = new StudentRequest("new", "new@new.com", "new", NEW_BIRTH_DATE, FEMALE);
        StudentPostgres student = new StudentPostgres("new", "new@email.com", "new", NEW_BIRTH_DATE, FEMALE);
        StudentPostgres differentNonNull = new StudentPostgres("different", "different@email.com", "new", NEW_BIRTH_DATE, FEMALE);

        String stringId = "1";
        long expectedId = 1L;

        when(studentMapper.requestToPostgres(request)).thenReturn(student);
        when(studentRepositoryPostgres.findById(expectedId)).thenReturn(Optional.of(differentNonNull));
        when(studentRepositoryPostgres.findByEmail(student.getEmail())).thenReturn(Optional.of(student));

        // THEN
        assertThrows(EmailTakenException.class, () -> studentServicePostgres.updateStudent(stringId, request));
        verify(studentRepositoryPostgres, never()).save(any());
    }

    @Test
    void updateStudent_InvalidStudentIdFormat() {
        // GIVEN
        StudentRequest request = new StudentRequest("new", "new@new.com", "new", NEW_BIRTH_DATE, FEMALE);
        String invalidId = "a";

        // THEN
        assertThrows(InvalidStudentIdFormatException.class, () -> studentServicePostgres.updateStudent(invalidId, request));
    }
}