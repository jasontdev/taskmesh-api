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
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import xyz.taskmesh.api.model.Tasklist;
import xyz.taskmesh.api.model.User;

import java.util.UUID;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserRepositoryTests {

    private DynamoDbTable<User> userTable;
    private UserRepository userRepository;
    private TasklistRepository tasklistRepository;
    private String tasklistid = "tasklist_" + UUID.randomUUID();

    public UserRepositoryTests(@Autowired UserRepository userRepository,
                               @Autowired TasklistRepository tasklistRepository,
                               @Autowired DynamoDbEnhancedClient dynamoDbEnhancedClient,
                               @Value("${database.tablename}") String tablename) {
        this.userRepository = userRepository;
        this.tasklistRepository = tasklistRepository;
        this.userTable = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(User.class));
    }

    @BeforeEach
    public void beforeEach() {
       userTable.createTable();
       var testTasklist = Tasklist.builder().tasklistId(tasklistid).build();
       tasklistRepository.save(testTasklist);
    }

    @AfterEach
    public void afterEach() {
        userTable.deleteTable();
    }

    @Test
    public void isInjectible() {
        Assertions.assertNotNull(userRepository);
    }

    @Test
    public void setupTest() {
        var tasklist = tasklistRepository.findById(tasklistid);
        Assertions.assertTrue(tasklist.isPresent());
        Assertions.assertEquals(tasklistid, tasklist.get().getTasklistId());
    }
}
