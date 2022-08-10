package dev.jasont.taskmesh.api.controller;

import dev.jasont.taskmesh.api.entity.Tasklist;
import dev.jasont.taskmesh.api.entity.User;
import dev.jasont.taskmesh.api.repository.TaskRepository;
import dev.jasont.taskmesh.api.repository.TasklistRepository;
import dev.jasont.taskmesh.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

@RestController
public class TasklistController {
    private TasklistRepository tasklistRepository;
    private UserRepository userRepository;
    private TaskRepository taskRepository;

    public TasklistController(@Autowired TasklistRepository tasklistRepository,
                              @Autowired UserRepository userRepository,
                              @Autowired TaskRepository taskRepository) {
        this.tasklistRepository = tasklistRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @PostMapping("/tasklist")
    public Tasklist createTasklist(@RequestBody Tasklist tasklist) {
        var newTasklist = new Tasklist();
        newTasklist.setName(tasklist.getName());

        if (tasklist.getTasks().size() > 0) {
            var tasks = taskRepository.saveAll(tasklist.getTasks());
            tasks.forEach(newTasklist::addTask);
        }

        var userIds = tasklist.getUsers().stream().map(User::getId).toList();
        var users = userRepository.findAllById(userIds);
        users.forEach(newTasklist::addUser);

        return tasklistRepository.save(newTasklist);
    }

    @GetMapping("/tasklist/{id}")
    public ResponseEntity<Tasklist> getTasklist(@PathVariable("id") Long tasklistId) {
        return ResponseEntity.of(tasklistRepository.findById(tasklistId));
    }
}
