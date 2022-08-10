package dev.jasont.taskmesh.api.controller;

import dev.jasont.taskmesh.api.entity.User;
import dev.jasont.taskmesh.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") String userId) {
        // TODO: check access token
        var user = userRepository.findById(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }

        var newUser = new User();
        newUser.setId(userId);
        userRepository.save(newUser);

        return ResponseEntity.of(userRepository.findById(userId));
    }
}
