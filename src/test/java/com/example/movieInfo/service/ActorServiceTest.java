package com.example.movieInfo.service;
import com.example.movieInfo.model.*;
import com.example.movieInfo.repository.ActorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ActorServiceTest {

    @Mock
    private ActorRepository actorRepository;

    @InjectMocks
    private ActorService actorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        try {
            Field instanceField = ActorService.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        actorService.init();
    }

    private Actor createActor(String name) {
        Actor actor = new Actor();
        actor.setName(name);
        return actor;
    }

    @Test
    public void testFindByNameOrCreate_ActorExists() {
        String actorName = "Leonardo DiCaprio";

        Actor existingActor = createActor(actorName);
        when(actorRepository.findByName(actorName)).thenReturn(Optional.of(existingActor));

        Actor result = actorService.findByNameOrCreate(actorName);

        assertNotNull(result);
        assertEquals(actorName, result.getName());
        verify(actorRepository, times(1)).findByName(actorName);
        verify(actorRepository, never()).save(any(Actor.class));
    }

    @Test
    public void testFindByNameOrCreate_ActorDoesNotExist() {
        String actorName = "Brad Pitt";
        when(actorRepository.findByName(actorName)).thenReturn(Optional.empty());
        Actor newActor = createActor(actorName);
        when(actorRepository.save(any(Actor.class))).thenReturn(newActor);
        Actor result = actorService.findByNameOrCreate(actorName);

        assertNotNull(result);
        assertEquals(actorName, result.getName());
        verify(actorRepository, times(1)).findByName(actorName);
        verify(actorRepository, times(1)).save(any(Actor.class));
    }

    @Test
    public void testGetInstance() {
        ActorService instance = ActorService.getInstance();
        assertNotNull(instance);
        assertEquals(actorService, instance);
    }
}
