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
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import xyz.taskmesh.api.model.Tasklist;
import xyz.taskmesh.api.model.TasklistUser;
import xyz.taskmesh.api.model.UserTasklist;

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

    @BeforeEach
    public void init() {
        dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(TasklistUser.class)).createTable();
    }

    @AfterEach
    public void clean() {
        dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(UserTasklist.class)).deleteTable();
    }

    @Test
    void taskRepositoryIsInjectible() {
        Assertions.assertNotNull(tasklistRepository);
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
    void findByUserIdSingleTasklist() {
        var userId = "user_" + UUID.randomUUID();
        var tasklistId = "tasklist_" + UUID.randomUUID();
        var tasklist = new Tasklist(tasklistId, userId, "Testing tasklist");
        tasklistRepository.create(tasklist); // TODO: remove dependency on testable method

        var tasklists = tasklistRepository.findUserTasklistByUserId(userId);
        Assertions.assertEquals(tasklist.getTasklistId(), tasklists.get(0).getTasklistId());
    }

    @Test
    void findByUserIdMultipleTasklists() {
        var userId = "user_" + UUID.randomUUID();
        var tasklistIdOne = "tasklist_" + UUID.randomUUID();
        var tasklistOne = new Tasklist(tasklistIdOne, userId, "Testing tasklist");
        tasklistRepository.create(tasklistOne); // TODO: remove dependency on testable method

        var tasklistIdTwo = "tasklist_" + UUID.randomUUID();
        var tasklistTwo = new Tasklist(tasklistIdTwo, userId, "Testing tasklist two");
        tasklistRepository.create(tasklistTwo); // TODO: remove dependency on testable method

        var tasklists = tasklistRepository.findUserTasklistByUserId(userId);
        Assertions.assertEquals(2, tasklists.size());
        Assertions.assertEquals(2, tasklists.stream()
                .filter(tasklist -> tasklist.getTasklistId().equals(tasklistOne.getTasklistId())
                        || tasklist.getTasklistId().equals(tasklistTwo.getTasklistId())).count());
    }

    @Test
    void findByUserIdNoLists() {
        Assertions.assertEquals(0, tasklistRepository.findUserTasklistByUserId("user_" + UUID.randomUUID()).size());
    }

    @Test
    void findTasklistUsers() {
        var userIdOne = "user_" + UUID.randomUUID();
        var userIdTwo = "user_" + UUID.randomUUID();
        var tasklistId = "tasklist_" + UUID.randomUUID();

        var table = dynamoDbEnhancedClient
                .table(tablename, TableSchema.fromBean(TasklistUser.class));

        table.putItem(new TasklistUser(tasklistId, userIdOne));
        table.putItem(new TasklistUser(tasklistId, userIdTwo));

        var users = tasklistRepository.findTasklistUserByTasklistId(tasklistId);

        Assertions.assertEquals(2, users.stream()
                .filter(user -> user.getUserId().equals(userIdOne) | user.getUserId().equals(userIdTwo)).count());
    }
}
