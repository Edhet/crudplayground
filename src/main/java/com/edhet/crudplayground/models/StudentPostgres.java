package com.edhet.crudplayground.models;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

@Entity
@Table(name = "students")
public class StudentPostgres {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "course", nullable = false)
    private String course;
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public StudentPostgres() {
    }

    public StudentPostgres(String name, String email, String course, LocalDate birthDate, Gender gender) {
        this.name = name;
        this.email = email;
        this.course = course;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public StudentPostgres(Long id, String name, String email, String course, LocalDate birthDate, Gender gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.course = course;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return Period.between(this.birthDate, LocalDate.now()).getYears();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentPostgres that = (StudentPostgres) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(course, that.course) && Objects.equals(birthDate, that.birthDate) && gender == that.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, course, birthDate, gender);
    }
}
