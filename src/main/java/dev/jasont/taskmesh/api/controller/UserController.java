package dev.jasont.taskmesh.api.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.jasont.taskmesh.api.entity.User;
import dev.jasont.taskmesh.api.service.UserService;
import dev.jasont.taskmesh.api.util.AuthenticatedUser;
import dev.jasont.taskmesh.api.util.UnauthourizedException;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(Principal token, @RequestBody User user) {
        // TODO: validate user before attempting to save
        try {
            var requestingUser = new AuthenticatedUser(token.getName());
            return ResponseEntity.of(userService.createUser(requestingUser, user));

        } catch (UnauthourizedException exception) {
            return ResponseEntity.status(403).build();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(Principal token,
            @PathVariable("id") String id) {
        try {
            var requestingUser = new AuthenticatedUser(token.getName());
            return ResponseEntity.of(userService.getUser(requestingUser, id));

        } catch (UnauthourizedException exception) {
            return ResponseEntity.status(403).build();
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }
}
