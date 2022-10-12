package dev.jasont.taskmesh.api.controller;

import java.security.Principal;

import dev.jasont.taskmesh.api.dto.NewTasklist;
import dev.jasont.taskmesh.api.dto.StoredTasklist;
import dev.jasont.taskmesh.api.util.TasklistMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.jasont.taskmesh.api.service.TasklistService;
import dev.jasont.taskmesh.api.util.AuthenticatedUser;
import dev.jasont.taskmesh.api.util.UnauthourizedException;

@RestController
public class TasklistController {
    private final TasklistService tasklistService;

    public TasklistController(@Autowired TasklistService tasklistService) {
        this.tasklistService = tasklistService;
    }

    @PostMapping("/tasklist")
    public ResponseEntity<StoredTasklist> createTasklist(Principal accessToken, @RequestBody NewTasklist newTasklist) {
        try {
            var authenticatedUser = new AuthenticatedUser(accessToken.getName());
            var tasklist = tasklistService.createTasklist(authenticatedUser, newTasklist);

            return tasklist.map(value -> ResponseEntity
                            .ok(TasklistMapper.mapTasklist().toStoredTasklist(value)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (UnauthourizedException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/tasklist/{id}")
    public ResponseEntity<StoredTasklist> getTasklist(Principal accessToken, @PathVariable("id") Long tasklistId) {
        try {
            var authenticatedUser = new AuthenticatedUser(accessToken.getName());
            var tasklist = tasklistService.getById(authenticatedUser, tasklistId);

            return tasklist.map(value -> ResponseEntity
                            .ok(TasklistMapper.mapTasklist().toStoredTasklist(value)))
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } catch (UnauthourizedException exception) {
            return ResponseEntity.badRequest().build();
        }
    }
}
