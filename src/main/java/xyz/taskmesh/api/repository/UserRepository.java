package xyz.taskmesh.api.repository;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import xyz.taskmesh.api.model.User;

import java.util.List;

@Repository
public class UserRepository extends DynamoDbRepository<User> {

    public UserRepository(@Autowired DynamoDbEnhancedClient dynamoDbEnhancedClient,
                          @Value("${database.tablename}") String tablename) {
        super(dynamoDbEnhancedClient, tablename, User.class);
    }

    public List<User> findAllByTasklistId(String tasklistId) {
        return findAllByKey(Key.builder().partitionValue(tasklistId).sortValue("user_").build());
    }
}
