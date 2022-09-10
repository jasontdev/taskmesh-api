package dev.jasont.taskmesh.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.jasont.taskmesh.api.entity.User;
import dev.jasont.taskmesh.api.repository.UserRepository;
import dev.jasont.taskmesh.api.util.UnauthourizedException;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("dev")
public class UserServiceTests {

    private UserService userService;
    private UserRepository userRepository;

    public UserServiceTests(@Autowired UserService userService, @Autowired UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @BeforeEach
    public void clearDatabase() {
        userRepository.deleteAll();
    }

    @Test
    public void isInjectible() {
        Assertions.assertNotNull(userService);
    }

    @Test
    public void createUser() throws UnauthourizedException {
        var user = new User();
        var userId = "user_id";
        user.setId(userId);

        var newUser = userService.createUser(userId, user);
        Assertions.assertTrue(newUser.isPresent());
        Assertions.assertEquals(userId, newUser.get().getId());
    }

    @Test
    public void getUserThatExists() throws UnauthourizedException {
        var user = new User();
        var userId = "user_id";
        user.setId(userId);
        userRepository.save(user);

        var savedUser = userService.getUser(userId, userId);
        Assertions.assertTrue(savedUser.isPresent());
        Assertions.assertEquals(userId, savedUser.get().getId());
    }

    @Test
    public void getUserThatDoesNotExist() {
        try {
            var userId = "user_id";
            var savedUser = userService.getUser(userId, userId);
            Assertions.assertFalse(savedUser.isPresent());
        } catch (UnauthourizedException e) {
        }
    }

    @Test
    public void getUserNotAuthorized() {
        var user = new User();
        var userId = "user_id";
        var requestingUserId = "wrong_user_id";
        user.setId(userId);
        userRepository.save(user);

        Assertions.assertThrows(UnauthourizedException.class,
                () -> userService.getUser(requestingUserId, userId));
    }
}
