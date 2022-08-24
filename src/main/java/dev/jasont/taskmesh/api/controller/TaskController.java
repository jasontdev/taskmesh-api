package dev.jasont.taskmesh.api.controller;

import dev.jasont.taskmesh.api.entity.Task;
import dev.jasont.taskmesh.api.entity.TaskInput;
import dev.jasont.taskmesh.api.repository.TaskRepository;
import dev.jasont.taskmesh.api.repository.TasklistRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class TaskController {

    private TaskRepository taskRepository;
    private TasklistRepository tasklistRepository;

    public TaskController(@Autowired TaskRepository taskRepository, 
    @Autowired TasklistRepository tasklistRepository) {
        this.taskRepository = taskRepository;
        this.tasklistRepository = tasklistRepository;
    }

    @PutMapping("/task")
    public ResponseEntity<Task> createTask(@RequestBody TaskInput taskInput) {
        var newTask = new Task();
        newTask.setName(taskInput.getName());

        var tasklist = tasklistRepository.findById(taskInput.getTasklistId());
        if(tasklist.isEmpty()) 
            return ResponseEntity.badRequest().build();
            
        tasklist.get().addTask(newTask);
        var savedTask = taskRepository.save(newTask);

        if(savedTask == null)
            return ResponseEntity.badRequest().build();

        return ResponseEntity.ok(savedTask);
    }

    @PatchMapping("/task")
    public ResponseEntity<Task> updateTask(@RequestBody Task task) {
        var savedTask = taskRepository.save(task);

        if(savedTask == null)
            return ResponseEntity.badRequest().build();
       
        return ResponseEntity.ok(savedTask);
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") Long id) {
        taskRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
