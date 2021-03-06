package xyz.taskmesh.api.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import xyz.taskmesh.api.model.Tasklist;
import xyz.taskmesh.api.model.TasklistUser;
import xyz.taskmesh.api.model.UserTasklist;

import java.util.List;
import java.util.Optional;

@Service
public class TasklistRepository {

    @Value("${database.tablename}")
    private String tablename;

    final
    DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public TasklistRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
    }

    public Optional<Tasklist> create(Tasklist tasklist) {
        var tasklistUser = new TasklistUser(tasklist.getTasklistId(), tasklist.getUserId());
        var userTasklist = new UserTasklist(tasklist.getUserId(), tasklist.getTasklistId(), tasklist.getName());

        try {
            dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(TasklistUser.class)).putItem(tasklistUser);
            dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(UserTasklist.class)).putItem(userTasklist);

            return Optional.of(tasklist);
        } catch (DynamoDbException e) {
            System.err.printf("tasklistRepository.create() error: %s\n", e.getMessage());
            return Optional.empty();
        }
    }

    public List<UserTasklist> findUserTasklistByUserId(String userId) {
        var conditions =
                QueryConditional.sortBeginsWith(Key.builder().partitionValue(userId).sortValue("tasklist_").build());
        var queryResult = dynamoDbEnhancedClient
                .table(tablename, TableSchema.fromBean(UserTasklist.class))
                .query(r -> r.queryConditional(conditions))
                .items();
        return queryResult.stream().toList();
    }

    public List<TasklistUser> findTasklistUserByTasklistId(String tasklistId) {
        var conditions =
                QueryConditional.sortBeginsWith((Key.builder().partitionValue(tasklistId).sortValue("user_").build()));
        var queryResult = dynamoDbEnhancedClient
                .table(tablename, TableSchema.fromBean(TasklistUser.class))
                .query(r -> r.queryConditional(conditions))
                .items();
        return queryResult.stream().toList();
    }
}
