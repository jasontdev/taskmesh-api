package xyz.taskmesh.api.repository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import xyz.taskmesh.api.model.Tasklist;
import xyz.taskmesh.api.model.TasklistUser;
import xyz.taskmesh.api.model.UserTasklist;

import java.util.Objects;
import java.util.UUID;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class TasklistRepositoryTests {

    @Autowired
    TasklistRepository tasklistRepository;

    @Autowired
    DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Value("${database.tablename}")
    private String tablename;

    @Test
    void taskRepositoryIsInjectible() {
        Assertions.assertNotNull(tasklistRepository);
    }

    @BeforeEach
    public void init() {
        dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(TasklistUser.class)).createTable();
    }

    @AfterEach
    public void clean() {
        dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(UserTasklist.class)).deleteTable();
    }

    @Test
    void saveTasklist() {
        var userId = "user_" + UUID.randomUUID();
        var tasklistId = "tasklist_" + UUID.randomUUID();
        var tasklist = new Tasklist(tasklistId, userId, "Testing, testing, 123");
        tasklistRepository.create(tasklist);

        var savedTasklistUser = dynamoDbEnhancedClient
                .table(tablename, TableSchema.fromBean(TasklistUser.class))
                .getItem(Key.builder().partitionValue(tasklistId).sortValue(userId).build());
        var savedUserTasklist = dynamoDbEnhancedClient
                .table(tablename, TableSchema.fromBean(UserTasklist.class))
                .getItem(Key.builder().partitionValue(userId).sortValue(tasklistId).build());

        Assertions.assertNotNull(savedTasklistUser);
        Assertions.assertNotNull(savedUserTasklist);
    }

    @Test
    void getAllByUserSingleTasklist() {
        var userId = "user_" + UUID.randomUUID();
        var tasklistId = "tasklist_" + UUID.randomUUID();
        var tasklist = new Tasklist(tasklistId, userId, "Testing tasklist");
        tasklistRepository.create(tasklist); // TODO: remove dependency on testable method

        var tasklists = tasklistRepository.findByUserId(userId);
        Assertions.assertEquals(tasklist, tasklists.get(0));
    }

    @Test
    void getAllByUserMultipleTasklists() {
        var userId = "user_" + UUID.randomUUID();
        var tasklistIdOne = "tasklist_" + UUID.randomUUID();
        var tasklistOne = new Tasklist(tasklistIdOne, userId, "Testing tasklist");
        tasklistRepository.create(tasklistOne); // TODO: remove dependency on testable method

        var tasklistIdTwo = "tasklist_" + UUID.randomUUID();
        var tasklistTwo = new Tasklist(tasklistIdTwo, userId, "Testing tasklist two");
        tasklistRepository.create(tasklistTwo); // TODO: remove dependency on testable method

        var tasklists = tasklistRepository.findByUserId(userId);
        Assertions.assertEquals(2, tasklists.stream()
                .filter(tasklist -> tasklist.equals(tasklistOne) || tasklist.equals(tasklistTwo)).count());
    }
}
