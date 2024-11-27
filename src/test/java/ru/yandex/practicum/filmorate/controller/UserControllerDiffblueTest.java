package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendsService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class UserControllerDiffblueTest {
    @MockBean
    private FriendsService friendsService;

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Test addFriend(long, long)")
    @Disabled("TODO: Complete this test")
    void testAddFriend() throws Exception {

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/users/{id}/friends/{friend_id}", 1L,
                1L);
        MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);
    }


    @DisplayName("Test updateUser(User)")
    @Disabled("TODO: Complete this test")
    void testUpdateUser() throws Exception {
        User user = new User(1L, "jane.doe@example.org", "Login", "Name", LocalDate.of(1970, 1, 1));
        user.setId(null);
        String content = (new ObjectMapper()).writeValueAsString(user);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);
    }

    @Test
    @DisplayName("Test removeFriend(long, long)")
    @Disabled("TODO: Complete this test")
    void testRemoveFriend() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/users/{id}/friends/{friend_id}", 1L,
                1L);
        MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);
    }

    @Test
    @DisplayName("Test getUsers()")
    @Disabled("TODO: Complete this test")
    void testGetUsers() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users");

        MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);
    }

    @Test
    @DisplayName("Test getUserById(long)")
    @Disabled("TODO: Complete this test")
    void testGetUserById() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/get/{id}", 1L);
        MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);
    }

    @Test
    @DisplayName("Test getFriends(long)")
    @Disabled("TODO: Complete this test")
    void testGetFriends() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/{id}/friends", 1L);
        MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);
    }

    @Test
    @DisplayName("Test getCommonFriends(Long, Long)")
    @Disabled("TODO: Complete this test")
    void testGetCommonFriends() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/{id}/friends/common/{otherId}",
                1L, 1L);
        MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);
    }

    @Test
    @DisplayName("Test addUser(User)")
    @Disabled("TODO: Complete this test")
    void testAddUser() throws Exception {
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.content(objectMapper
                .writeValueAsString(new User(1L, "jane.doe@example.org", "Login", "Name", LocalDate.of(1970, 1, 1))));
        MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);
    }
}
