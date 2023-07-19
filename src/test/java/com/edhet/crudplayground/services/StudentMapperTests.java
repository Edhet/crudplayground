package com.edhet.crudplayground.services;

import com.edhet.crudplayground.dtos.StudentDTO;
import com.edhet.crudplayground.models.StudentMongo;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;

import static com.edhet.crudplayground.models.Gender.MALE;
import static org.junit.jupiter.api.Assertions.*;

public class StudentMapperTests {

    private final StudentMapper studentMapper = new StudentMapper();

    @Test
    void itShouldMapDtoToMongo() {
        // GIVEN
        StudentDTO dto = new StudentDTO("id", "name", "email@email.com", "course", LocalDate.of(2000, 1, 1), null, MALE);
        int age = Period.between(dto.birthDate(), LocalDate.now()).getYears();

        // WHEN
        StudentMongo mongo = studentMapper.dtoToMongo(dto);

        // THEN
        StudentMongo expected = new StudentMongo("id", "name", "email@email.com", "course", LocalDate.of(2000, 1, 1), MALE);
        assertEquals(expected, mongo);
        assertEquals(age, mongo.getAge());
    }
}
