package com.edhet.crudplayground.services;

import com.edhet.crudplayground.dtos.StudentDTO;
import com.edhet.crudplayground.dtos.StudentRequest;
import com.edhet.crudplayground.models.StudentMongo;
import com.edhet.crudplayground.models.StudentPostgres;
import org.springframework.stereotype.Service;

@Service
public class StudentMapper {

    public StudentMapper() {
    }

    public StudentPostgres requestToPostgres(StudentRequest studentRequest) {
        return new StudentPostgres(
                studentRequest.name(),
                studentRequest.email(),
                studentRequest.course(),
                studentRequest.birthDate(),
                studentRequest.gender()
        );
    }

    public StudentDTO postgresToDto(StudentPostgres studentPostgres) {
        return new StudentDTO(
                studentPostgres.getId().toString(),
                studentPostgres.getName(),
                studentPostgres.getEmail(),
                studentPostgres.getCourse(),
                studentPostgres.getBirthDate(),
                studentPostgres.getAge(),
                studentPostgres.getGender()
        );
    }

    public StudentMongo requestToMongo(StudentRequest studentRequest) {
        return new StudentMongo(
                studentRequest.name(),
                studentRequest.email(),
                studentRequest.course(),
                studentRequest.birthDate(),
                studentRequest.gender()
        );
    }

    public StudentDTO mongoToDto(StudentMongo studentMongo) {
        return new StudentDTO(
                studentMongo.getId(),
                studentMongo.getName(),
                studentMongo.getEmail(),
                studentMongo.getCourse(),
                studentMongo.getBirthDate(),
                studentMongo.getAge(),
                studentMongo.getGender()
        );
    }
}
