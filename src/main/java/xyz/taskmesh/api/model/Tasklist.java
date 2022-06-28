package xyz.taskmesh.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tasklist {
    private String tasklistId;
    private List<User> users;
    private List<Task> tasks;
    private String title;

    public Tasklist(List<User> users, List<Task> tasks, String title) {
        tasklistId = "tasklist_".concat(UUID.randomUUID().toString());
        this.users = users;
        this.tasks = tasks;
        this.title = title;
    }

    public Tasklist(List<User> users, List<Task> tasks) {
        tasklistId = "tasklist_".concat(UUID.randomUUID().toString());
        this.users = users;
        this.tasks = tasks;
    }

    public Tasklist(List<User> users) {
        tasklistId = "tasklist_".concat(UUID.randomUUID().toString());
        this.users = users;
        this.tasks = new ArrayList<>();
    }

    public Tasklist(String title) {
        tasklistId = "tasklist_".concat(UUID.randomUUID().toString());
        this.users = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.title = title;
    }

    public Tasklist() {
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
