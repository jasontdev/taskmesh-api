package xyz.taskmesh.api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import xyz.taskmesh.api.model.TasklistUser;

import java.util.List;

@Repository
public class TasklistUserRepository extends DynamoDbRepository<TasklistUser> {

    public TasklistUserRepository(@Autowired DynamoDbEnhancedClient dynamoDbEnhancedClient,
                                  @Value("${database.tablename}") String tablename) {
        super(dynamoDbEnhancedClient, tablename, TasklistUser.class);
    }

    public List<TasklistUser> findAllByTasklistId(String tasklistId) {
        return findAllByKey(Key.builder().partitionValue(tasklistId).sortValue("user_").build());
    }
}
