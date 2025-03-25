package com.example.movieInfo.repository;

import com.example.movieInfo.model.Director;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DirectorRepository extends JpaRepository<Director, Long> {
    Optional<Director> findByName(String name);
}
