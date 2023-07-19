package com.edhet.crudplayground.dtos;

import com.edhet.crudplayground.models.Gender;

import java.time.LocalDate;

public record StudentDTO(
        String id,
        String name,
        String email,
        String course,
        LocalDate birthDate,
        Integer age,
        Gender gender
) {
}
