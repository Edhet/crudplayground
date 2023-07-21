package com.edhet.crudplayground.services;

import com.edhet.crudplayground.exceptions.InvalidDatabaseSelectedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceFactoryTest {

    @Mock
    private StudentServiceMongoImpl studentServiceMongo;
    @Mock
    private StudentServicePostgresImpl studentServicePostgres;

    private StudentServiceFactory studentServiceFactory;

    @BeforeEach
    void setUp() {
        studentServiceFactory = new StudentServiceFactory(studentServiceMongo, studentServicePostgres);
    }

    @Test
    void getStudentService_Mongo() {
        // GIVEN
        String database = "MONGO";

        // WHEN
        StudentService returnedImpl = studentServiceFactory.getStudentService(database);

        // THEN
        assertEquals(studentServiceMongo, returnedImpl);
    }

    @Test
    void getStudentService_Postgres() {
        // GIVEN
        String database = "POSTGRES";

        // WHEN
        StudentService returnedImpl = studentServiceFactory.getStudentService(database);

        // THEN
        assertEquals(studentServicePostgres, returnedImpl);
    }

    @Test
    void getStudentService_InvalidDatabaseSelected() {
        // GIVEN
        String database = "error";

        // THEN
        assertThrows(InvalidDatabaseSelectedException.class, () -> studentServiceFactory.getStudentService(database));
    }
}