package dev.jasont.taskmesh.api.util;

import java.util.List;

import dev.jasont.taskmesh.api.dto.NewTask;
import dev.jasont.taskmesh.api.dto.StoredTask;
import dev.jasont.taskmesh.api.entity.Task;
import dev.jasont.taskmesh.api.entity.Tasklist;

public class TaskMapper {
    public static StoredTask taskToStoredTask(Task task) {
        return new StoredTask(task.getId(), task.getTasklist().getId(), task.getName(), false);
    }

    public static List<StoredTask> taskToStoredTasks(List<Task> tasks) {
        return tasks.stream().map(TaskMapper::taskToStoredTask).toList();
    }

    public static List<Task> newTasksToTasks(List<NewTask> newTasks, Tasklist tasklist) {
        return newTasks.stream().map(newTask -> TaskMapper.newTaskToTask(newTask, tasklist)).toList();
    }

    private static Task newTaskToTask(NewTask newTask, Tasklist tasklist) {
        var task = new Task(newTask.name());
        task.setComplete(false);
        tasklist.addTask(task);

        return task;
    }
}
