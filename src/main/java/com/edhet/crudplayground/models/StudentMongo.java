package com.edhet.crudplayground.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

@Document("students")
public class StudentMongo {
    @Id
    private String id;
    private String name;
    private String email;
    private String course;
    private LocalDate birthDate;
    private Gender gender;

    public StudentMongo() {
    }

    public StudentMongo(String name, String email, String course, LocalDate birthDate, Gender gender) {
        this.name = name;
        this.email = email;
        this.course = course;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public StudentMongo(String id, String name, String email, String course, LocalDate birthDate, Gender gender) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.course = course;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "StudentMongo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", course='" + course + '\'' +
                ", birthDate=" + birthDate +
                ", gender=" + gender +
                '}';
    }

    public String getId() {
        return id;
    }

    public Integer getAge() {
        return Period.between(this.birthDate, LocalDate.now()).getYears();
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentMongo that = (StudentMongo) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(course, that.course) && Objects.equals(birthDate, that.birthDate) && gender == that.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, course, birthDate, gender);
    }
}
