package com.edhet.crudplayground.services;

import com.edhet.crudplayground.dtos.StudentDTO;
import com.edhet.crudplayground.dtos.StudentRequest;
import com.edhet.crudplayground.exceptions.InvalidStudentIdFormatException;
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

    public StudentPostgres dtoToPostgres(StudentDTO studentDTO) throws InvalidStudentIdFormatException {
        long id;
        try {
            id = Long.parseLong(studentDTO.id());
        } catch (NumberFormatException e) {
            throw new InvalidStudentIdFormatException(studentDTO.id() + " is not a valid Postgres ID");
        }

        return new StudentPostgres(
                id,
                studentDTO.name(),
                studentDTO.email(),
                studentDTO.course(),
                studentDTO.birthDate(),
                studentDTO.gender()
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

    public StudentMongo dtoToMongo(StudentDTO studentDTO) {
        return new StudentMongo(
                studentDTO.id(),
                studentDTO.name(),
                studentDTO.email(),
                studentDTO.course(),
                studentDTO.birthDate(),
                studentDTO.gender()
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
