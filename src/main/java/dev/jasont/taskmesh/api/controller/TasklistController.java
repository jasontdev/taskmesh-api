package dev.jasont.taskmesh.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.jasont.taskmesh.api.entity.Tasklist;
import dev.jasont.taskmesh.api.service.TasklistService;
import dev.jasont.taskmesh.api.util.AuthenticatedUser;

@RestController
public class TasklistController {
    private TasklistService tasklistService;

    public TasklistController(@Autowired TasklistService tasklistService) {
        this.tasklistService = tasklistService;
    }

    @PostMapping("/tasklist")
    public ResponseEntity<Tasklist> createTasklist(@AuthenticationPrincipal OAuth2AuthenticatedPrincipal accessToken, @RequestBody Tasklist tasklist) {
        var authenticatedUser = new AuthenticatedUser(accessToken.getAttribute("sub"));
        return ResponseEntity.of(tasklistService.createTasklist(authenticatedUser, tasklist));
    }

    @GetMapping("/tasklist/{id}")
    public ResponseEntity<Tasklist> getTasklist(@AuthenticationPrincipal OAuth2AuthenticatedPrincipal accessToken, @PathVariable("id") Long tasklistId) {
        var authenticatedUser = new AuthenticatedUser(accessToken.getAttribute("sub"));
        return ResponseEntity.of(tasklistService.getById(authenticatedUser, tasklistId));
    }
}
