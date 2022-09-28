package dev.jasont.taskmesh.api.dto;

public record StoredTask(Long id, Long tasklistId, String name, Boolean isComplete) {
}
