package dev.jasont.taskmesh.api.repository;

import dev.jasont.taskmesh.api.entity.Tasklist;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TasklistRepository extends JpaRepository<Tasklist, Long> {
    List<Tasklist> findAllByUsersId(String id);
}
