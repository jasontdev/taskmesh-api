package dev.jasont.taskmesh.api.entity;

import java.util.ArrayList;
import java.util.List;

public class TasklistInput {
    private List<String> userIds = new ArrayList<String>();
    private String name;
    private List<Task> tasks;

    public TasklistInput(String name) {
        this.name = name;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
