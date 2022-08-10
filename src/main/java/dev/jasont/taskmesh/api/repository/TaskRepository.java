package dev.jasont.taskmesh.api.repository;

import dev.jasont.taskmesh.api.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
