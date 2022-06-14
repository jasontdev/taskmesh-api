package xyz.taskmesh.api.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

@SpringBootTest
public class DynamoDBConfigurationTests {

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    @Test
    void enhancedClientInjectible() {
        Assertions.assertNotNull(enhancedClient);
    }
}
