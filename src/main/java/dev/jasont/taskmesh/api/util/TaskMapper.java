package dev.jasont.taskmesh.api.util;

import java.util.List;

import dev.jasont.taskmesh.api.dto.NewTask;
import dev.jasont.taskmesh.api.dto.StoredTask;
import dev.jasont.taskmesh.api.entity.Task;
import dev.jasont.taskmesh.api.entity.Tasklist;

public class TaskMapper {
    public static StoredTask fromTask(Task task) {
        return new StoredTask(task.getId(), task.getTasklist().getId(), task.getName(), false);
    }

    public static List<StoredTask> fromTasks(List<Task> tasks) {
        return tasks.stream().map(TaskMapper::fromTask).toList();
    }

    public static List<Task> fromNewTasks(List<NewTask> newTasks, Tasklist tasklist) {
        return newTasks.stream().map(newTask -> TaskMapper.fromNewTask(newTask, tasklist)).toList();
    }

    private static Task fromNewTask(NewTask newTask, Tasklist tasklist) {
        var task = new Task(newTask.name());
        task.setComplete(false);
        tasklist.addTask(task);

        return task;
    }
}
