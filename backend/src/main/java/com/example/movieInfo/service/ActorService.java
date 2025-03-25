package com.example.movieInfo.service;

import com.example.movieInfo.exception.ResourceNotFoundException;
import com.example.movieInfo.model.Actor;
import com.example.movieInfo.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActorService {
    @Autowired
    private ActorRepository actorRepository;

    private static ActorService instance;

    @Autowired
    public void init() {
        instance = this;
    }

    public static ActorService getInstance() {
        return instance;
    }

    public Actor findByNameOrCreate(String name) {
        Optional<Actor> actor = actorRepository.findByName(name);
        return actor.orElseGet(() -> {
            Actor newActor = new Actor();
            newActor.setName(name);
            return actorRepository.save(newActor);
        });
    }
}
