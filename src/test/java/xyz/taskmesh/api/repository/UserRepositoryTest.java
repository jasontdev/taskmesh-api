package xyz.taskmesh.api.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import xyz.taskmesh.api.model.Tasklist;
import xyz.taskmesh.api.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserRepositoryTest {

    private DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private String tablename;
    private UserRepository userRepository;
    private TasklistRepository tasklistRepository;

    public UserRepositoryTest(@Autowired DynamoDbEnhancedClient dynamoDbEnhancedClient,
                              @Value("${database.tablename}") String tablename,
                              @Autowired UserRepository userRepository,
                              @Autowired TasklistRepository tasklistRepository) {
       this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
       this.tablename = tablename;
       this.userRepository = userRepository;
       this.tasklistRepository = tasklistRepository;
    }

    @BeforeEach
    public void beforeEach() {
        dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(User.class)).createTable();
    }

    @AfterEach
    public void afterEach() {
        dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(User.class)).deleteTable();
    }

    @Test
    public void isInjectible() {
        Assertions.assertNotNull(userRepository);
    }

    @Test
    public void findById() {
        var tasklistId = "tasklist_" + UUID.randomUUID();
        var userId = "user_" + UUID.randomUUID();
        var user = new User();
        user.setTasklistId(tasklistId);
        user.setUserId(userId);
        var users = new ArrayList<User>();
        users.add(user);
        var tasklist = new Tasklist();
        tasklist.setTasklistId(tasklistId);
        tasklist.setUsers(users);
        tasklistRepository.save(tasklist);
        var savedUsers = Optional.ofNullable(userRepository.findAllByTasklistId(tasklistId));
        Assertions.assertTrue(savedUsers.isPresent());
        Assertions.assertTrue(savedUsers.get().contains(user));
    }
}
