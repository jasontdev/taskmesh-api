package xyz.taskmesh.api.repository;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class DynamoDbRepository<T> {

    protected final DynamoDbTable<T> table;

    public DynamoDbRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient, String tablename, Class<T> tClass) {
        this.table = dynamoDbEnhancedClient.table(tablename, TableSchema.fromBean(tClass));
    }

    public Optional<T> save(T item) {
        try {
            table.putItem(item);
            return Optional.of(item);
        } catch (DynamoDbException e) {
            return Optional.empty();
        }
    }

    public Optional<T> update(T item) {
        try {
            table.updateItem(item);
            return Optional.of(item);
        } catch (DynamoDbException e) {
            return Optional.empty();
        }
    }

    public Optional<T> delete(T item) {
        try {
            table.deleteItem(item);
            return Optional.of(item);
        } catch (DynamoDbException e) {
            return Optional.empty();
        }
    }

    public List<T> findAllByKey(Key key) {
        try {
            var query = table.query(QueryConditional.sortBeginsWith(key));
            return query.items().stream().toList();
        } catch (DynamoDbException e) {
            return new ArrayList<T>();
        }
    }
}
