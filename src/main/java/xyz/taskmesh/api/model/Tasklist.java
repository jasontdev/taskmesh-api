package xyz.taskmesh.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
public class Tasklist {
    private String tasklistId;
    private List<User> users = new ArrayList<>();
    private List<Task> tasks = new ArrayList<>();
    private String title;

    public Tasklist(String tasklistId, List<User> users, List<Task> tasks, String title) {
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
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
