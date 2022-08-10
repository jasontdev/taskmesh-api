package dev.jasont.taskmesh.api.repository;

import dev.jasont.taskmesh.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
