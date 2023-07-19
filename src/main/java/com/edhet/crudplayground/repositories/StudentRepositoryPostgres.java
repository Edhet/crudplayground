package com.edhet.crudplayground.repositories;

import com.edhet.crudplayground.models.StudentPostgres;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StudentRepositoryPostgres extends CrudRepository<StudentPostgres, Long> {

    @Query("SELECT s FROM StudentPostgres s WHERE s.email = :email")
    Optional<StudentPostgres> findByEmail(String email);
}
