package com.edhet.crudplayground.repositories;

import com.edhet.crudplayground.models.StudentMongo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.time.LocalDate;
import java.util.Optional;

import static com.edhet.crudplayground.models.Gender.MALE;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class StudentRepositoryMongoTest {

    @Autowired
    private StudentRepositoryMongo underTest;

    @Test
    void itShouldfindByEmail() {
        // GIVEN
        StudentMongo student = new StudentMongo("name", "email@email.com", "course", LocalDate.of(2000, 1, 1), MALE);

        // WHEN
        underTest.save(student);
        Optional<StudentMongo> result = underTest.findByEmail(student.getEmail());

        // THEN
        assertTrue(result.isPresent());
    }
}