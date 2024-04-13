package edu.tcu.cs.hogwartsartifactsonline.hogwartsuser;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.hasSize;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static  org.mockito.BDDMockito.given;
import  static  org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    List<HogwartsUser> users;

    @Value("${api.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        this.users = new ArrayList<>();

        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1);
        u1.setUsername("john");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");
        this.users.add(u1);

        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2);
        u2.setUsername("eric");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRoles("user");
        this.users.add(u2);


        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3);
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRoles("user");
        this.users.add(u3);

    }

    @Test
    void testFindAllUsersSuccess() throws Exception {
        given(userService.findAll()).willReturn(users);

        mockMvc.perform(get(baseUrl + "/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find all Success"))
                .andExpect(jsonPath("$.data", hasSize(users.size())))
                .andExpect(jsonPath("$.data[0].id").value(users.get(0).getId()))
                .andExpect(jsonPath("$.data[0].username").value(users.get(0).getUsername()))
                .andExpect(jsonPath("$.data[1].id").value(users.get(1).getId()))
                .andExpect(jsonPath("$.data[1].username").value(users.get(1).getUsername()));
    }

    @Test
    void testFindUserByIdSuccess() throws Exception {
        given(userService.findById(1)).willReturn(users.get(0));

        mockMvc.perform(get(baseUrl + "/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(users.get(0).getId()))
                .andExpect(jsonPath("$.data.username").value(users.get(0).getUsername()));
    }

    @Test
    void testFindUserByIdNotFound() throws Exception {
        given(userService.findById(5)).willThrow(new ObjectNotFoundException());

        mockMvc.perform(get(baseUrl + "/users/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with Id 5 :("))
                .andExpect(jsonPath("$.data").doesNotExist());
    }
}
@Test
void testAddUserSuccess() throws Exception {
    HogwartsUser user = new HogwartsUser();
    user.setId(4);
    user.setUsername("lily");
    user.setPassword("123456"); // Corrected typo in setPassword
    user.setEnabled(true);
    user.setRoles("admin user");

    String json = this.objectMapper.writeValueAsString(user);

    given(userService.save(any(HogwartsUser.class))).willReturn(user);

    mockMvc.perform(post("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json) // Changed from objectMapper.writeValueAsString(users)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Add Success"))
            .andExpect(jsonPath("$.data.id").value(user.getId()))
            .andExpect(jsonPath("$.data.username").value(user.getUsername())); // Corrected to getUsername
}

@Test
void testUpdatedUserSuccess() throws Exception {
    UserDto userDto = new UserDto(3, "tom123", false);

    HogwartsUser updatedUser = new HogwartsUser();
    updatedUser.setId(3);
    updatedUser.setUsername("tom123");
    updatedUser.setEnabled(false);
    updatedUser.setRoles("user");

    String json = this.objectMapper.writeValueAsString(userDto);

    given(this.userService.update(eq(3), Mockito.any(HogwartsUser.class))).willReturn(updatedUser);

    mockMvc.perform(put("/api/v1/users/3")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.flag").value(true))
            .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
            .andExpect(jsonPath("$.message").value("Update Success"))
            .andExpect(jsonPath("$.data.id").value(updatedUser.getId()))
            .andExpect(jsonPath("$.data.username").value(updatedUser.getUsername()));
}

@Test
void testUpdateUserErrorWithNonExistentId() throws Exception {
    given(this.userService.update(eq(5), any(HogwartsUser.class))).willThrow(new ObjectNotFoundException(5));

    UserDto userDto = new UserDto(5,"tom123", false);

    String json = this.objectMapper.writeValueAsString(userDto);

    this.mockMvc.perform(put("/api/v1/users/5")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.flag").value(false))
            .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
            .andExpect(jsonPath("$.message").value("Could not find user with Id 5 :("));
}
