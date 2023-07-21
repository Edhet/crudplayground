package com.edhet.crudplayground.repositories;

import com.edhet.crudplayground.models.StudentMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepositoryMongo extends MongoRepository<StudentMongo, String> {

    Optional<StudentMongo> findByEmail(String email);
}
