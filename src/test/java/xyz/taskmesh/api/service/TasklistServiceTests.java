package xyz.taskmesh.api.service;

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
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import xyz.taskmesh.api.model.TasklistUser;
import xyz.taskmesh.api.model.UserTasklist;

import java.util.ArrayList;
import java.util.UUID;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")

public class TasklistServiceTests {

    private final TasklistService tasklistService;
    private final DynamoDbTable<UserTasklist> userTasklistTable;
    private final DynamoDbTable<TasklistUser> tasklistUserTable;

    public TasklistServiceTests(@Autowired TasklistService tasklistService,
                                @Value("${database.tablename}") String tablename,
                                @Autowired DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.tasklistService = tasklistService;
        this.userTasklistTable = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(UserTasklist.class));
        this.tasklistUserTable = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(TasklistUser.class));
    }

    @BeforeEach
    public void beforeEach() {
        userTasklistTable.createTable();
    }

    @AfterEach
    public void afterEach() {
        userTasklistTable.deleteTable();
    }

    @Test
    public void isInjectable() {
        Assertions.assertNotNull(tasklistService);
    }

    @Test
    public void createTasklist() {
        var userIdOne = "user_" + UUID.randomUUID();
        var userIdTwo = "user_" + UUID.randomUUID();
        var userIds = new ArrayList<String>();
        userIds.add(userIdOne);
        userIds.add(userIdTwo);
        var tasklist = tasklistService.create("Test tasklist", userIds);

        Assertions.assertTrue(tasklist.isPresent());

        var userTasklistOne = userTasklistTable
                .getItem(Key.builder().partitionValue(userIdOne).sortValue(tasklist.get()).build());
        Assertions.assertEquals("Test tasklist", userTasklistOne.getName());

        var userTasklistTwo = userTasklistTable
                .getItem(Key.builder().partitionValue(userIdTwo).sortValue(tasklist.get()).build());
        Assertions.assertEquals("Test tasklist", userTasklistTwo.getName());

        var tasklistUserConditonal =
                QueryConditional.sortBeginsWith(Key.builder().partitionValue(tasklist.get()).sortValue("user_").build());
        var tasklistUserQuery = tasklistUserTable.query(tasklistUserConditonal);
        var usersMatch = tasklistUserQuery.items().stream()
                .allMatch(i -> i.getUserId().equals(userIdOne) | i.getUserId().equals(userIdTwo));
        Assertions.assertTrue(usersMatch);
    }
}
