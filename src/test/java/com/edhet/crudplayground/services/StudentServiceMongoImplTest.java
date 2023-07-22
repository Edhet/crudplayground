package com.edhet.crudplayground.services;

import com.edhet.crudplayground.dtos.RequestDTO;
import com.edhet.crudplayground.exceptions.EmailTakenException;
import com.edhet.crudplayground.exceptions.InvalidBirthDateException;
import com.edhet.crudplayground.exceptions.StudentNotFoundException;
import com.edhet.crudplayground.models.StudentMongo;
import com.edhet.crudplayground.repositories.StudentRepositoryMongo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private final LocalDate NEW_BIRTH_DATE = LocalDate.of(2020, 1, 1);
    private final LocalDate BIRTH_DATE = LocalDate.of(2000, 1, 1);
    private final LocalDate INVALID_DATE = LocalDate.now().plusDays(1);

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
        StudentMongo expected = new StudentMongo("name", "email@email.com", "course", BIRTH_DATE, MALE);
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
        RequestDTO request = new RequestDTO("name", "email@email.com", "course", BIRTH_DATE, MALE);
        StudentMongo expected = new StudentMongo("name", "email@email.com", "course", BIRTH_DATE, MALE);

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
        RequestDTO student = new RequestDTO("name", "email@email.com", "course", BIRTH_DATE, MALE);
        StudentMongo expected = new StudentMongo("name", "email@email.com", "course", BIRTH_DATE, MALE);

        when(studentRepositoryMongo.findByEmail(student.email())).thenReturn(Optional.of(expected));

        // THEN
        assertThrows(EmailTakenException.class, () -> studentServiceMongo.addStudent(student));
        verify(studentMapper, never()).requestToMongo(any());
        verify(studentRepositoryMongo, never()).save(any());
    }


    @Test
    void addStudent_InvalidBirthDate() {
        // GIVEN
        RequestDTO student = new RequestDTO("name", "email@email.com", "course", INVALID_DATE, MALE);

        when(studentRepositoryMongo.findByEmail(student.email())).thenReturn(Optional.empty());

        // THEN
        assertThrows(InvalidBirthDateException.class, () -> studentServiceMongo.addStudent(student));
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
        RequestDTO request = new RequestDTO("new", "new@new.com", "new", NEW_BIRTH_DATE, FEMALE);
        StudentMongo student = new StudentMongo("name", "email@email.com", "course", BIRTH_DATE, MALE);

        StudentMongo expected = new StudentMongo("new", "new@email.com", "new", NEW_BIRTH_DATE, FEMALE);

        String id = "id";

        when(studentMapper.requestToMongo(request)).thenReturn(expected);
        when(studentRepositoryMongo.findById(id)).thenReturn(Optional.of(student));

        // WHEN
        studentServiceMongo.updateStudent(id, request);

        // THEN
        verify(studentMapper).requestToMongo(request);
        verify(studentRepositoryMongo).findById(id);
        assertEquals(expected, student);
        verify(studentRepositoryMongo).save(student);
    }

    @Test
    void updateStudent_StudentNotFound() {
        //GIVEN
        RequestDTO request = new RequestDTO("new", "new@new.com", "new", NEW_BIRTH_DATE, FEMALE);
        String id = "id";
        when(studentRepositoryMongo.findById(id)).thenReturn(Optional.empty());

        //THEN
        assertThrows(StudentNotFoundException.class, () -> studentServiceMongo.updateStudent(id, request));
        verify(studentRepositoryMongo, never()).save(any());
    }

    @Test
    void updateStudent_EmailTaken() {
        // GIVEN
        RequestDTO request = new RequestDTO("new", "new@new.com", "new", NEW_BIRTH_DATE, FEMALE);
        StudentMongo student = new StudentMongo("new", "new@email.com", "new", NEW_BIRTH_DATE, FEMALE);
        StudentMongo differentNonNull = new StudentMongo("different", "different@email.com", "new", NEW_BIRTH_DATE, FEMALE);

        String id = "id";

        when(studentMapper.requestToMongo(request)).thenReturn(student);
        when(studentRepositoryMongo.findById(id)).thenReturn(Optional.of(differentNonNull));
        when(studentRepositoryMongo.findByEmail(student.getEmail())).thenReturn(Optional.of(student));

        // THEN
        assertThrows(EmailTakenException.class, () -> studentServiceMongo.updateStudent(id, request));
        verify(studentRepositoryMongo, never()).save(any());
    }

    @Test
    void updateStudent_InvalidBirthDate() {
        //GIVEN
        RequestDTO request = new RequestDTO("new", "new@new.com", "new", NEW_BIRTH_DATE, FEMALE);
        StudentMongo student = new StudentMongo("new", "new@email.com", "new", INVALID_DATE, FEMALE);

        String id = "id";

        when(studentRepositoryMongo.findById(id)).thenReturn(Optional.of(student));
        when(studentMapper.requestToMongo(request)).thenReturn(student);

        //THEN
        assertThrows(InvalidBirthDateException.class, () -> studentServiceMongo.updateStudent(id, request));
        verify(studentRepositoryMongo, never()).save(any());
    }

}