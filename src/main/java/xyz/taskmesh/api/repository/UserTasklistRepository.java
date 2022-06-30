package xyz.taskmesh.api.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import xyz.taskmesh.api.model.UserTasklist;

import java.util.List;

public class UserTasklistRepository extends DynamoDbRepository<UserTasklist> {

    public UserTasklistRepository(@Autowired DynamoDbEnhancedClient dynamoDbEnhancedClient,
                                  @Value("${database.tablename}") String tablename) {
        super(dynamoDbEnhancedClient, tablename, UserTasklist.class);
    }

    public List<UserTasklist> findAllByUserId(String userId) {
        return findAllByKey(Key.builder().partitionValue(userId).sortValue("tasklist_").build());
    }
}
