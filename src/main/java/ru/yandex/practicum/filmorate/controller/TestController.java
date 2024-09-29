package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;

@RestController
@RequestMapping("/test")
public class TestController {


    private final UserService userService;

    public TestController(UserService userService) {
        this.userService = userService;
    }
}
