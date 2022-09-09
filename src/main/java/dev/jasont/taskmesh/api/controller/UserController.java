package dev.jasont.taskmesh.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jasont.taskmesh.api.entity.User;
import dev.jasont.taskmesh.api.service.UserService;
import dev.jasont.taskmesh.api.util.UnauthorizedException;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@AuthenticationPrincipal OAuth2AuthenticatedPrincipal token, User user) {
        // TODO: validate user before attempting to save
        try {
            var newUser = userService.createUser(token, user);
            return ResponseEntity.of(newUser);
        } catch (UnauthorizedException exception) {
            return ResponseEntity.status(403).build();
        }
    }
}
