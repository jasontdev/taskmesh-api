package xyz.taskmesh.api.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
public class Tasklist {
    private String tasklistId;
    private List<TasklistUser> users = new ArrayList<>();
    private List<Task> tasks = new ArrayList<>();
    private String title;

    public Tasklist(String tasklistId, List<TasklistUser> users, List<Task> tasks, String title) {
        this.tasklistId = tasklistId;
        if (users != null) {
            this.users = users;
        } else {
            this.users = new ArrayList<>();
        }
        if (tasks != null) {
            this.tasks = tasks;
        } else {
            this.tasks = new ArrayList<>();
        }
        this.title = title;
    }

    public List<TasklistUser> getUsers() {
        return users;
    }

    public void setUsers(List<TasklistUser> users) {
        this.users = users;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getTasklistId() {
        return tasklistId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
