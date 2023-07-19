package com.edhet.crudplayground.services;


import com.edhet.crudplayground.exceptions.InvalidDatabaseSelectedException;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceFactory {
    enum AvailableDatabases {MONGO, POSTGRES}

    private final StudentServiceMongoImpl studentServiceMongoImpl;
    private final StudentServicePostgresImpl studentServicePostgresImpl;

    public StudentServiceFactory(StudentServiceMongoImpl studentServiceMongoImpl, StudentServicePostgresImpl studentServicePostgresImpl) {
        this.studentServiceMongoImpl = studentServiceMongoImpl;
        this.studentServicePostgresImpl = studentServicePostgresImpl;
    }

    public StudentService getStudentService(String database) {
        AvailableDatabases selectedDb;
        try {
            selectedDb = AvailableDatabases.valueOf(database.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidDatabaseSelectedException("This application does not support " + database + " database");
        }

        return switch (selectedDb) {
            case MONGO -> studentServiceMongoImpl;
            case POSTGRES -> studentServicePostgresImpl;
        };
    }

}
