package dev.jasont.taskmesh.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import dev.jasont.taskmesh.api.entity.Tasklist;
import dev.jasont.taskmesh.api.entity.User;
import dev.jasont.taskmesh.api.repository.TasklistRepository;
import dev.jasont.taskmesh.api.repository.UserRepository;
import dev.jasont.taskmesh.api.util.AuthenticatedUser;
import dev.jasont.taskmesh.api.util.UnauthourizedException;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTests {

    private UserService userService;
    private UserRepository userRepository;
    private TasklistRepository tasklistRepository;

    public UserServiceTests(@Autowired UserService userService, @Autowired UserRepository userRepository,
            @Autowired TasklistRepository tasklistRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.tasklistRepository = tasklistRepository;
    }

    @BeforeEach
    public void clearDatabase() {
        tasklistRepository.deleteAll();
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
        var authUser = new AuthenticatedUser(userId);

        var newUser = userService.createUser(authUser, user);
        Assertions.assertTrue(newUser.isPresent());
        Assertions.assertEquals(userId, newUser.get().getId());
    }

    @Test
    public void createUserNotAuthorised() {
        var user = new User("user_id");
        var authUser = new AuthenticatedUser("wrong_user_id");

        Assertions.assertThrows(UnauthourizedException.class, () -> userService.createUser(authUser, user));
    }

    @Test
    public void getUserThatExists() throws UnauthourizedException {
        var userId = "user_id";
        var authUser = new AuthenticatedUser(userId);
        var tasklist = new Tasklist("Test tasklist");
        var user = new User(userId);

        user.addTasklist(tasklist);
        tasklistRepository.save(tasklist);

        var savedUser = userService.getUser(authUser, userId);
        Assertions.assertTrue(savedUser.isPresent());
        Assertions.assertEquals(1, savedUser.get().getTasklists().size());
    }

    @Test
    public void getUserThatDoesNotExist() throws UnauthourizedException {
        var userId = "user_id";
        var authUser = new AuthenticatedUser(userId);
        var savedUser = userService.getUser(authUser, userId);
        Assertions.assertFalse(savedUser.isPresent());
    }

    @Test
    public void getUserNotAuthorized() {
        var user = new User("user_id");
        var authUser = new AuthenticatedUser("wrong_user_id");
        userRepository.save(user);

        Assertions.assertThrows(UnauthourizedException.class,
                () -> userService.getUser(authUser, "user_id"));
    }
}
