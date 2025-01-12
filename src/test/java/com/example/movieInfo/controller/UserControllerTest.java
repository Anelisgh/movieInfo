package com.example.movieInfo.controller;

import com.example.movieInfo.dto.UserRequest;
import com.example.movieInfo.dto.UserResponse;
import com.example.movieInfo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    private UserRequest createUserRequest(String name, String email) {
        return new UserRequest(name, email);
    }

    private UserResponse createUserResponse(String name, String email) {
        return new UserResponse(name, email);
    }

    @Test
    public void testCreateUser() throws Exception {
        UserRequest userRequest = createUserRequest("John Doe", "john.doe@example.com");
        UserResponse userResponse = createUserResponse("John Doe", "john.doe@example.com");

        when(userService.createUser(any(UserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\", \"email\": \"john.doe@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"name\": \"John Doe\", \"email\": \"john.doe@example.com\"}"));
    }

    @Test
    public void testGetUserById() throws Exception {
        UserResponse userResponse = createUserResponse("John Doe", "john.doe@example.com");

        when(userService.getUserById(anyLong())).thenReturn(userResponse);

        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"name\": \"John Doe\", \"email\": \"john.doe@example.com\"}"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserRequest userRequest = createUserRequest("John Doe", "john.doe@example.com");
        UserResponse userResponse = createUserResponse("John Doe", "john.doe@example.com");

        when(userService.updateUser(anyLong(), any(UserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(put("/users/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\", \"email\": \"john.doe@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"name\": \"John Doe\", \"email\": \"john.doe@example.com\"}"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/users/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
