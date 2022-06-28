package xyz.taskmesh.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")

public class TasklistServiceTests {

    private final TasklistService tasklists;

    public TasklistServiceTests(@Autowired TasklistService tasklists,
                                @Value("${database.tablename}") String tablename,
                                @Autowired DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.tasklists = tasklists;
    }

    @Test
    public void isInjectable() {
        Assertions.assertNotNull(tasklists);
    }
}


