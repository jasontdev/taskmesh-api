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
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import xyz.taskmesh.api.model.UserTasklist;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserTasklistRepositoryTests {

    private final DynamoDbTable<UserTasklist> userTasklistTable;
    private final UserTasklistRepository userTasklistRepository;

    public UserTasklistRepositoryTests(@Autowired UserTasklistRepository userTasklistRepository,
                                       @Autowired TasklistRepository tasklistRepository,
                                       @Autowired DynamoDbEnhancedClient dynamoDbEnhancedClient,
                                       @Value("${database.tablename}") String tablename) {
        this.userTasklistTable = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(UserTasklist.class));
        this.userTasklistRepository = userTasklistRepository;
    }

    @BeforeEach
    public void beforeEach() {
        this.userTasklistTable.createTable();
    }

    @AfterEach
    public void afterEach() {
        this.userTasklistTable.deleteTable();
    }

    @Test
    public void isInjectable() {
        Assertions.assertNotNull(userTasklistRepository);
    }
}

