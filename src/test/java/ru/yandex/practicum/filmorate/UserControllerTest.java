package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private InMemoryUserStorage inMemoryUserStorage;

    @Mock
    private UserService userService;

    private User user;

    @BeforeEach
    void testSetUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLogin("testLogin");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(2000, 1, 1));
    }

    @Test
    void testGetUsers() {
        when(inMemoryUserStorage.getUsers()).thenReturn(Arrays.asList(user));

        Collection<User> users = userController.getUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(user, users.iterator().next());
        verify(inMemoryUserStorage, times(1)).getUsers();
    }

    @Test
    void testAddUser() {
        when(inMemoryUserStorage.addUser(any(User.class))).thenReturn(user);

        User createdUser = userController.addUser(user);

        assertNotNull(createdUser);
        assertEquals(user.getId(), createdUser.getId());
        verify(inMemoryUserStorage, times(1)).addUser(user);
    }

    @Test
    void testUpdateUser() {
        when(inMemoryUserStorage.updateUser(any(User.class))).thenReturn(user);

        User updatedUser = userController.updateUser(user);

        assertNotNull(updatedUser);
        assertEquals(user.getId(), updatedUser.getId());
        verify(inMemoryUserStorage, times(1)).updateUser(user);
    }

    @Test
    void testSetUserById() {
        when(inMemoryUserStorage.getUserById(1L)).thenReturn(user);

        User foundUser = userController.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        verify(inMemoryUserStorage, times(1)).getUserById(1L);
    }

    @Test
    void testAddFriend() {
        when(userService.addFriend(1L, 2L)).thenReturn(Arrays.asList(user));

        Collection<User> friends = userController.addFriend(1L, 2L);

        assertNotNull(friends);
        assertEquals(1, friends.size());
        verify(userService, times(1)).addFriend(1L, 2L);
    }

    @Test
    void testGetFriends() {
        when(userService.getFriends(1L)).thenReturn(Arrays.asList(user));

        Collection<User> friends = userController.getFriends(1L);

        assertNotNull(friends);
        assertEquals(1, friends.size());
        verify(userService, times(1)).getFriends(1L);
    }

    @Test
    void testDeleteFriend() {
        when(userService.deleteFriend(1L, 2L)).thenReturn("Friend deleted");

        String response = userController.deleteFriend(1L, 2L);

        assertEquals("Friend deleted", response);
        verify(userService, times(1)).deleteFriend(1L, 2L);
    }

    @Test
    void testGetCommonFriends() {
        when(userService.getCommonFriends(1L, 2L)).thenReturn(Arrays.asList(user));

        Collection<User> commonFriends = userController.getCommonFriends(1L, 2L);

        assertNotNull(commonFriends);
        assertEquals(1, commonFriends.size());
        verify(userService, times(1)).getCommonFriends(1L, 2L);
    }
}