package dev.jasont.taskmesh.api.repository;

import dev.jasont.taskmesh.api.entity.Tasklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasklistRepository extends JpaRepository<Tasklist, Long> {
}
