package com.edhet.crudplayground.repositories;

import com.edhet.crudplayground.models.StudentPostgres;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static com.edhet.crudplayground.models.Gender.MALE;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentRepositoryPostgresTest {
    private final LocalDate BIRTH_DATE = LocalDate.of(2000, 1, 1);

    @Autowired
    private StudentRepositoryPostgres studentRepositoryPostgres;

    @AfterEach
    void tearDown() {
        studentRepositoryPostgres.deleteAll();
    }

    @Test
    void shouldFindByEmail() {
        // GIVEN
        StudentPostgres student = new StudentPostgres("name", "email@email.com", "course", BIRTH_DATE, MALE);

        // WHEN
        studentRepositoryPostgres.save(student);
        Optional<StudentPostgres> result = studentRepositoryPostgres.findByEmail(student.getEmail());

        // THEN
        assertTrue(result.isPresent());
    }

    @Test
    void shouldNotFindByEmail() {
        // GIVEN
        String email = "email@email.com";

        // WHEN
        Optional<StudentPostgres> result = studentRepositoryPostgres.findByEmail(email);

        // THEN
        assertFalse(result.isPresent());
    }
}