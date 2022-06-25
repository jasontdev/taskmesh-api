package xyz.taskmesh.api.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public abstract class RepositoryTests<T> {

    protected final DynamoDbTable<T> table;

    public RepositoryTests(DynamoDbEnhancedClient dynamoDbEnhancedClient,
                           String tablename,
                           Class<T> tClass) {
        this.table = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(tClass));
    }

    @BeforeEach
    public void init() {
        table.createTable();
    }

    @AfterEach
    public void clean() {
        table.deleteTable();
    }
}
