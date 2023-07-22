package com.edhet.crudplayground.services;

import com.edhet.crudplayground.dtos.StudentDTO;
import com.edhet.crudplayground.dtos.RequestDTO;
import com.edhet.crudplayground.models.StudentMongo;
import com.edhet.crudplayground.models.StudentPostgres;
import org.junit.jupiter.api.Test;

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
        RequestDTO requestDTO = new RequestDTO("name", "email@email.com", "course", BIRTH_DATE, MALE);
        StudentPostgres expected = new StudentPostgres("name", "email@email.com", "course", BIRTH_DATE, MALE);

        // THEN
        assertEquals(expected, studentMapper.requestToPostgres(requestDTO));
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
        RequestDTO requestDTO = new RequestDTO("name", "email@email.com", "course", BIRTH_DATE, MALE);
        StudentMongo expected = new StudentMongo("name", "email@email.com", "course", BIRTH_DATE, MALE);

        // THEN
        assertEquals(expected, studentMapper.requestToMongo(requestDTO));
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