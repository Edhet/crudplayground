package com.edhet.crudplayground.dtos;

import com.edhet.crudplayground.models.Gender;

import java.time.LocalDate;

public record StudentRequest(
        String name,
        String email,
        String course,
        LocalDate birthDate,
        Gender gender
) { }
