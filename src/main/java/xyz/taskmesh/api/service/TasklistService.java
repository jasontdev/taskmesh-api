package xyz.taskmesh.api.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import xyz.taskmesh.api.model.TasklistUser;
import xyz.taskmesh.api.model.UserTasklist;
import xyz.taskmesh.api.repository.TasklistUserRepository;
import xyz.taskmesh.api.repository.UserTasklistRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TasklistService {

    private final TasklistUserRepository tasklistUserRepository;
    private final UserTasklistRepository userTasklistRepository;

    public TasklistService(TasklistUserRepository tasklistUserRepository, UserTasklistRepository userTasklistRepository) {
        this.tasklistUserRepository = tasklistUserRepository;
        this.userTasklistRepository = userTasklistRepository;
    }

    public Optional<String> create(String name, List<String> users) {
        var tasklistId = "tasklist_" + UUID.randomUUID();
        try {
            users.forEach(userId -> userTasklistRepository.save(new UserTasklist(userId, tasklistId, name)));
            users.forEach(userId -> tasklistUserRepository.save(new TasklistUser(tasklistId, userId)));
            return Optional.of(tasklistId);
        } catch (DynamoDbException e) {
            return Optional.empty();
        }
    }
}
