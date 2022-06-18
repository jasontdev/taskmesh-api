package xyz.taskmesh.api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import xyz.taskmesh.api.model.Task;
import xyz.taskmesh.api.model.Tasklist;
import xyz.taskmesh.api.model.TasklistUser;
import xyz.taskmesh.api.model.UserTasklist;

import java.util.UUID;

@Service
public class TasklistRepository {

    @Value("${database.tablename}")
    private String tablename;

    @Autowired
    DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public Tasklist create(Tasklist tasklist) {
        var tasklistUser = new TasklistUser();
        tasklistUser.setTasklistId(tasklist.getTasklistId());
        tasklistUser.setUserId(tasklist.getUserId());

        var userTasklist = new UserTasklist();
        userTasklist.setUserId(tasklist.getUserId());
        userTasklist.setTasklistId(tasklist.getTasklistId());
        userTasklist.setName(tasklist.getName());

        try {
            dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(TasklistUser.class)).putItem(tasklistUser);
            dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(UserTasklist.class)).putItem(userTasklist);

            return tasklist;
        } catch(DynamoDbException e) {
            System.err.printf("tasklistRepository.create() error: %s\n", e.getMessage());

            return null;
        }
    }
}
