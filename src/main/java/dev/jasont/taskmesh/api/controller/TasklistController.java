package dev.jasont.taskmesh.api.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.jasont.taskmesh.api.entity.Tasklist;
import dev.jasont.taskmesh.api.entity.TasklistInput;
import dev.jasont.taskmesh.api.service.TasklistService;
import dev.jasont.taskmesh.api.util.AuthenticatedUser;
import dev.jasont.taskmesh.api.util.UnauthourizedException;

@RestController
public class TasklistController {
    private TasklistService tasklistService;

    public TasklistController(@Autowired TasklistService tasklistService) {
        this.tasklistService = tasklistService;
    }

    @PostMapping("/tasklist")
    public ResponseEntity<Tasklist> createTasklist(Principal accessToken, @RequestBody TasklistInput tasklistInput) {
        try {
            var authenticatedUser = new AuthenticatedUser(accessToken.getName());
            return ResponseEntity.of(tasklistService.createTasklist(authenticatedUser, tasklistInput));
        } catch (UnauthourizedException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/tasklist/{id}")
    public ResponseEntity<Tasklist> getTasklist(Principal accessToken, @PathVariable("id") Long tasklistId) {
        try {
            var authenticatedUser = new AuthenticatedUser(accessToken.getName());
            return ResponseEntity.of(tasklistService.getById(authenticatedUser, tasklistId));
        } catch (UnauthourizedException exception) {
            return ResponseEntity.badRequest().build();
        }
    }
}
