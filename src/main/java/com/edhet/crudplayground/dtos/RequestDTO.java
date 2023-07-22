package com.edhet.crudplayground.dtos;

import com.edhet.crudplayground.models.Gender;

import java.time.LocalDate;

public record RequestDTO(
        String name,
        String email,
        String course,
        LocalDate birthDate,
        Gender gender
) { }
