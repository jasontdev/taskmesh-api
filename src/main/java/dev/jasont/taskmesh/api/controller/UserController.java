package dev.jasont.taskmesh.api.controller;

import java.security.Principal;

import dev.jasont.taskmesh.api.dto.NewUser;
import dev.jasont.taskmesh.api.dto.StoredUser;
import dev.jasont.taskmesh.api.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<StoredUser> createUser(Principal token, @RequestBody NewUser newUser) {
        // TODO: validate user before attempting to save
        try {
            var requestingUser = new AuthenticatedUser(token.getName());
            var savedUser = userService.createUser(requestingUser, UserMapper.fromNewUser(newUser));
            return savedUser.map(user -> ResponseEntity.ok(UserMapper.fromUserToDTO(user))).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (UnauthourizedException exception) {
            return ResponseEntity.status(403).build();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<StoredUser> getUser(Principal token,
            @PathVariable("id") String id) {
        try {
            var requestingUser = new AuthenticatedUser(token.getName());
            var storedUser = userService.getUser(requestingUser, id);
            return storedUser.map(user -> ResponseEntity.ok(UserMapper.fromUserToDTO(user))).orElseGet(() -> ResponseEntity.notFound().build());

        } catch (UnauthourizedException exception) {
            return ResponseEntity.status(403).build();
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }
}
