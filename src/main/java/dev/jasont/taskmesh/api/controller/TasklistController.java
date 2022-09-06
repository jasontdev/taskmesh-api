package dev.jasont.taskmesh.api.controller;

import dev.jasont.taskmesh.api.entity.Tasklist;
import dev.jasont.taskmesh.api.entity.User;
import dev.jasont.taskmesh.api.repository.TaskRepository;
import dev.jasont.taskmesh.api.repository.TasklistRepository;
import dev.jasont.taskmesh.api.repository.UserRepository;
import dev.jasont.taskmesh.api.service.TasklistService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class TasklistController {
    private TasklistRepository tasklistRepository;
    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private TasklistService tasklistService;

    public TasklistController(@Autowired TasklistRepository tasklistRepository,
                              @Autowired UserRepository userRepository,
                              @Autowired TaskRepository taskRepository,
                              @Autowired TasklistService tasklistService) {
        this.tasklistRepository = tasklistRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.tasklistService = tasklistService;
    }

    @PostMapping("/tasklist")
    public ResponseEntity<Tasklist> createTasklist(@AuthenticationPrincipal OAuth2AuthenticatedPrincipal accessToken, @RequestBody Tasklist tasklist) {
        return ResponseEntity.of(tasklistService.createTasklist(accessToken, tasklist));
    }

    @GetMapping("/tasklist/{id}")
    public ResponseEntity<Tasklist> getTasklist(@PathVariable("id") Long tasklistId) {
        return ResponseEntity.of(tasklistRepository.findById(tasklistId));
    }
}
