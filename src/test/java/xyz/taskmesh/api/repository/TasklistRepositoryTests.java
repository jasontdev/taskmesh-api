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
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import xyz.taskmesh.api.model.Metadata;
import xyz.taskmesh.api.model.Tasklist;
import xyz.taskmesh.api.model.User;

import java.util.ArrayList;
import java.util.UUID;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class TasklistRepositoryTests {

    private final TasklistRepository tasklistRepository;
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private final String tablename;

    public TasklistRepositoryTests(@Autowired TasklistRepository tasklistRepository,
                                   @Autowired DynamoDbEnhancedClient dynamoDbEnhancedClient,
                                   @Value("${database.tablename}") String tablename) {
        this.tasklistRepository = tasklistRepository;
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
        this.tablename = tablename;
    }

    @BeforeEach
    public void beforeEach() {
        dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(Metadata.class)).createTable();
    }

    @AfterEach
    public void afterEach() {
        dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(Metadata.class)).deleteTable();
    }

    @Test
    public void isInjectible() {
        Assertions.assertNotNull(tasklistRepository);
    }

    @Test
    public void saveTasklist() {
        var tasklist = new Tasklist();
        tasklist.setTasklistId("tasklist_" + UUID.randomUUID().toString());
        tasklist.setTitle("Test tasklist");
        var user = new User("user_" + UUID.randomUUID().toString(), tasklist.getTasklistId());
        var users = new ArrayList<User>();
        users.add(user);
        tasklist.setUsers(users);
        tasklistRepository.save(tasklist);

        var table = dynamoDbEnhancedClient
                .table(tablename, TableSchema.fromBean(Metadata.class));
        var item = table
                .getItem(Key.builder().partitionValue(tasklist.getTasklistId()).sortValue("metadata").build());
        Assertions.assertEquals(tasklist.getTitle(), item.getTitle());

        var userTable = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(User.class));
        var items = userTable
                .query(QueryConditional.sortBeginsWith(
                        Key.builder().partitionValue(tasklist.getTasklistId()).sortValue("user_").build())).items();
        Assertions.assertEquals(1, items.stream().count());
    }

    @Test
    public void findById() {
        var tasklistId = "tasklist_" + UUID.randomUUID();
        var title = "findById() test tasklist";
        var tasklist = new Tasklist();
        tasklist.setTitle(title);
        tasklist.setTasklistId(tasklistId);

        var userIdOne = "user_" + UUID.randomUUID();
        var userOne = new User();
        userOne.setTasklistId(tasklistId);
        userOne.setUserId(userIdOne);

        var userIdTwo = "user_" + UUID.randomUUID();
        var userTwo = new User();
        userTwo.setTasklistId(tasklistId);
        userTwo.setUserId(userIdTwo);

        var users = new ArrayList<User>();
        users.add(userOne);
        users.add(userTwo);

        tasklist.setUsers(users);

        tasklistRepository.save(tasklist); // TODO: replace with direct DDB code
        var savedTasklist = tasklistRepository.findById(tasklistId);

        Assertions.assertTrue(savedTasklist.isPresent());
        Assertions.assertEquals(2, savedTasklist.get().getUsers().stream().count());
        Assertions.assertEquals(0, savedTasklist.get().getTasks().stream().count());
    }
}
