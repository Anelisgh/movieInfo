package com.example.movieInfo.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="directors")
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name", unique = true, nullable = false)
    private String name;
    @Column(name="birth_date")
    private LocalDate birthDate;
    @Column(name="debut_year")
    private Integer debutYear;
    @Column(name="is_active")
    private Boolean isActive = true;
    @OneToMany(mappedBy = "director")
    private List<Movie> movies;

    public Director(){
    }

    public Director(String name, LocalDate birthDate, int debutYear, boolean isActive, List<Movie> movies) {
        this.name = name;
        this.birthDate = birthDate;
        this.debutYear = debutYear;
        this.isActive = isActive;
        this.movies = movies;
    }

    public Director(String name) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public Integer getDebutYear() {
        return debutYear;
    }

    public void setDebutYear(Integer debutYear) {
        this.debutYear = debutYear;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}

