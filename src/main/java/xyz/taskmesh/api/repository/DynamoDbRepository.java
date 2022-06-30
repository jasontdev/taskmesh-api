package xyz.taskmesh.api.repository;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.Optional;

public abstract class DynamoDbRepository<T> {

    private DynamoDbTable<T> dynamoDbTable;

    public DynamoDbRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient,
                              String tablename,
                              Class<T> tClass) {
        this.dynamoDbTable = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(tClass));
    }

    public void save(T item) {
        dynamoDbTable.putItem(item);
    }

    public void update(T item) {
        dynamoDbTable.updateItem(item);
    }

    public void delete(T item) {
        dynamoDbTable.deleteItem(item);
    }

    public Optional<T> get(T item) {
        var savedItem = dynamoDbTable.getItem(item);
        return Optional.ofNullable(savedItem);
    }

    public Optional<T> findByKey(Key key) {
        var savedItem = dynamoDbTable.getItem(key);
        return Optional.ofNullable(savedItem);
    }

    public List<T> findAllByKey(Key key) {
        return dynamoDbTable.query(
                QueryConditional.sortBeginsWith(key)
        ).items().stream().toList();
    }
}
