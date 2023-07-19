package com.edhet.crudplayground.repositories;

import com.edhet.crudplayground.models.StudentMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentRepositoryMongo extends MongoRepository<StudentMongo, String> {

    Optional<StudentMongo> findByEmail(String email);
}
