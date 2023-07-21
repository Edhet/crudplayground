package com.edhet.crudplayground.services;

import com.edhet.crudplayground.dtos.StudentDTO;
import com.edhet.crudplayground.dtos.StudentRequest;
import com.edhet.crudplayground.models.Gender;
import com.edhet.crudplayground.models.StudentMongo;
import com.edhet.crudplayground.models.StudentPostgres;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Period;

import static com.edhet.crudplayground.models.Gender.MALE;
import static org.junit.jupiter.api.Assertions.*;

class StudentMapperTest {

    private final StudentMapper studentMapper = new StudentMapper();
    private final LocalDate BIRTH_DATE = LocalDate.of(2000, 1, 1);

    @Test
    void requestToPostgres() {
        // GIVEN
        StudentRequest studentRequest = new StudentRequest("name", "email@email.com", "course", BIRTH_DATE, MALE);
        StudentPostgres expected = new StudentPostgres("name", "email@email.com", "course", BIRTH_DATE, MALE);

        // THEN
        assertEquals(expected, studentMapper.requestToPostgres(studentRequest));
    }

    @Test
    void postgresToDto() {
        // GIVEN
        StudentPostgres studentPostgres = new StudentPostgres(1L, "name", "email@email.com", "course", BIRTH_DATE, MALE);
        StudentDTO expected = new StudentDTO("1", "name", "email@email.com", "course", BIRTH_DATE, Period.between(BIRTH_DATE, LocalDate.now()).getYears(), MALE);

        // THEN
        assertEquals(expected, studentMapper.postgresToDto(studentPostgres));
    }

    @Test
    void requestToMongo() {
        // GIVEN
        StudentRequest studentRequest = new StudentRequest("name", "email@email.com", "course", BIRTH_DATE, MALE);
        StudentMongo expected = new StudentMongo("name", "email@email.com", "course", BIRTH_DATE, MALE);

        // THEN
        assertEquals(expected, studentMapper.requestToMongo(studentRequest));
    }

    @Test
    void mongoToDto() {
        // GIVEN
        StudentMongo studentMongo = new StudentMongo("id", "name", "email@email.com", "course", BIRTH_DATE, MALE);
        StudentDTO expected = new StudentDTO("id", "name", "email@email.com", "course", BIRTH_DATE, Period.between(BIRTH_DATE, LocalDate.now()).getYears(), MALE);

        // THEN
        assertEquals(expected, studentMapper.mongoToDto(studentMongo));
    }
}