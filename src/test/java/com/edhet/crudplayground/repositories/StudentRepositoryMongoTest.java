package com.edhet.crudplayground.repositories;

import com.edhet.crudplayground.models.StudentMongo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDate;
import java.util.Optional;

import static com.edhet.crudplayground.models.Gender.MALE;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class StudentRepositoryMongoTest {
    private final LocalDate BIRTH_DATE = LocalDate.of(2000, 1, 1);

    @Autowired
    private StudentRepositoryMongo studentRepositoryMongo;

    @AfterEach
    void tearDown() {
        studentRepositoryMongo.deleteAll();
    }

    @Test
    void shouldFindByEmail() {
        // GIVEN
        StudentMongo student = new StudentMongo("name", "email@email.com", "course", BIRTH_DATE, MALE);

        // WHEN
        studentRepositoryMongo.save(student);
        Optional<StudentMongo> result = studentRepositoryMongo.findByEmail(student.getEmail());

        // THEN
        assertTrue(result.isPresent());
    }

    @Test
    void shouldNotFindByEmail() {
        // GIVEN
        String email = "email@email.com";

        // WHEN
        Optional<StudentMongo> result = studentRepositoryMongo.findByEmail(email);

        // THEN
        assertFalse(result.isPresent());
    }
}