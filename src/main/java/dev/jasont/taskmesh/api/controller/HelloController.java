package dev.jasont.taskmesh.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String secured() {
        return "Congratulations, you're secure and authenticated.";
    }
}