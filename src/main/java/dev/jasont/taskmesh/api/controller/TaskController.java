package dev.jasont.taskmesh.api.controller;

import dev.jasont.taskmesh.api.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {

    private TaskRepository taskRepository;

    public TaskController(@Autowired TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
}
