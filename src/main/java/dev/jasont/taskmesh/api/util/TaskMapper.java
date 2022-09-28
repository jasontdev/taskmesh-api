package dev.jasont.taskmesh.api.util;

import java.util.List;

import dev.jasont.taskmesh.api.dto.StoredTask;
import dev.jasont.taskmesh.api.entity.Task;

public class TaskMapper {
    public static StoredTask fromTask(Task task) {
        return new StoredTask(task.getId(), task.getTasklist().getId(), task.getName(), false);
    }

    public static List<StoredTask> fromTasks(List<Task> tasks) {
        return tasks.stream().map(task -> {
            return fromTask(task);
        }).toList();
    }
}
