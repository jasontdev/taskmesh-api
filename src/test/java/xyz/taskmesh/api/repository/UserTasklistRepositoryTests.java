package xyz.taskmesh.api.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import xyz.taskmesh.api.model.UserTasklist;

import java.util.UUID;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserTasklistRepositoryTests extends RepositoryTests<UserTasklist> {

    private final UserTasklistRepository userTasklistRepository;

    public UserTasklistRepositoryTests(@Autowired UserTasklistRepository userTasklistRepository,
                                       @Autowired DynamoDbEnhancedClient dynamoDbEnhancedClient,
                                       @Value("${database.tablename}") String tablename) {
        super(dynamoDbEnhancedClient, tablename, UserTasklist.class);
        this.userTasklistRepository = userTasklistRepository;
    }

    @Test
    public void isInjectible() {
        Assertions.assertNotNull(userTasklistRepository);
    }

    @Test
    public void save() {
        var userId = "user_" + UUID.randomUUID();
        var tasklistId = "tasklist_" + UUID.randomUUID();
        var name = "This list has a name";
        var userTasklist = new UserTasklist(userId, tasklistId, name);

        userTasklistRepository.save(userTasklist);

        Assertions.assertEquals(name, table.getItem(userTasklist).getName());
    }

    @Test
    public void findUserTasklistByTasklistId() {
        var userId = "user_" + UUID.randomUUID();
        var tasklistId = "tasklist_" + UUID.randomUUID();
        var name = "List 1";
        var userTasklist = new UserTasklist(userId, tasklistId, name);
        table.putItem(userTasklist);
        table.putItem(new UserTasklist("tasklist_" + tasklistId,
                "user_" + UUID.randomUUID(), "List 2"));

        var query = table.query(QueryConditional.sortBeginsWith(Key.builder().partitionValue(tasklistId).sortValue("user_").build()));
        Assertions.assertTrue(query.items().stream().allMatch(i -> i.getTasklistId().equals(tasklistId)));
        Assertions.assertTrue(query.items().stream().allMatch(i -> i.getName().equals("List 1") | i.getName().equals("List 2")));
    }
}
