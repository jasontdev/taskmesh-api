package xyz.taskmesh.api.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class DynamoDBConfigurationTests {

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    @Test
    void enhancedClientInjectible() {
        Assertions.assertNotNull(enhancedClient);
    }
}
