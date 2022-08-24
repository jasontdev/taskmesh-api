package dev.jasont.taskmesh.api.entity;

public class TaskInput {

    private String name;
    private Long tasklistId;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTasklistId(Long tasklistId) {
        this.tasklistId = tasklistId;
    }
    public Long getTasklistId() {
        return tasklistId;
    }
}
